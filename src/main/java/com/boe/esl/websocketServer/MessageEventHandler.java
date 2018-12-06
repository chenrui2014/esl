package com.boe.esl.websocketServer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import com.boe.esl.model.*;
import com.boe.esl.service.GatewayService;
import com.boe.esl.socket.struct.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.boe.esl.service.LabelService;
import com.boe.esl.socket.ESLSocketUtils;
import com.boe.esl.socket.NettyConstant;
import com.boe.esl.socket.ServerHandler;
import com.boe.esl.socket.struct.ESLHeader;
import com.boe.esl.socket.struct.ESLMessage;
import com.boe.esl.socket.struct.HeaderType;
import com.boe.esl.utils.JsonConverter;
import com.boe.esl.vo.OperatorVO;
import com.boe.esl.vo.UpdateVO;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;

import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息事件处理器
 * 
 * @ClassName MessageEventHandler
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年8月15日 下午5:27:39
 * @lastUpdate 2018年8月15日 下午5:27:39
 */
@Component
@Slf4j
public class MessageEventHandler {

	private SocketIOServer server;

	private Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

	@Autowired
	private LabelService labelService;

	@Autowired
	private GatewayService gatewayService;

	@Autowired
	@Qualifier("serverHandler")
	private ServerHandler serverHandler;

	private Map<String, Timer> timerMap;

	@Value("esl.network-timeout")
	private int networkTimeout;//组网超时时间

	@Autowired
	public MessageEventHandler(SocketIOServer server) {

		this.server = server;
		timerMap = new HashMap<>();
	}

	@OnConnect
	public void onConnect(SocketIOClient client) {
		if (client != null) {
			String username = client.getHandshakeData().getSingleUrlParam("username");
			String password = client.getHandshakeData().getSingleUrlParam("password");
			String sessionId = client.getSessionId().toString();
			clientMap.put(sessionId, client);
			log.info("连接成功, username=" + username + ", password=" + password + ", sessionId=" + sessionId);
		} else {
			log.error("客户端为空");
		}
	}

	@OnDisconnect
	public void onDisConnect(SocketIOClient client) {
		log.info("断开连接");
		client.disconnect();
	}

	@OnEvent(value = "sendControl")
	public void onControlEvent(SocketIOClient client, AckRequest ackRequest, ControlMessage controlMessage) {
		log.info("处理控制指令");
		Label label = labelService.getLabelByMac(controlMessage.getLabelMac());
		Gateway gateway = null;
		if(label != null){
			gateway = label.getGateway();
			if(gateway != null){
				Map<String, SocketChannel> regChannelGroup = serverHandler.getRegChannelGroup();
				SocketChannel ch = regChannelGroup.get(gateway.getKey());
				ESLMessage controlMsg = new ESLMessage();
				ESLHeader header = new ESLHeader();
				header.setCode(HeaderType.REQ);
				header.setType(MessageType.CONTROL);
				byte[] content = ESLSocketUtils.createControlContent(controlMessage).array();
				header.setLength((byte) content.length);
				controlMsg.setEslHeader(header);
				controlMsg.setContent(content);
				ch.writeAndFlush(controlMsg);
			}else{
				client.sendEvent("sendControl","该标签没有入网");
			}
		}else {
			client.sendEvent("sendControl","没有找到对应的标签");
		}
	}

	@OnEvent(value = "sendNetwork")
	public void onNetworkEvent(SocketIOClient client, AckRequest ackRequest, Map<String,List<String>> networkMap){

		log.info("处理组网事件");
		if(networkMap != null){
			networkMap.forEach((k,v) ->{
				Gateway gateway = gatewayService.getGatewayByKey(k);
				if(gateway == null){
					client.sendEvent("sendNetwork","网关不存在");
				}else{
					List<Label> labelList = new ArrayList<>();
					v.forEach(code ->{
						Label label = labelService.getLabelByCode(code);
						if(label != null){
							label.setGateway(gateway);
							label.setStatus(LabelStatus.NETWORKING.getCode());
							labelList.add(label);
						}
					});
					//批量插入
					labelService.save(labelList);
				}
				Map<String, SocketChannel> regChannelGroup = serverHandler.getRegChannelGroup();
				SocketChannel ch = regChannelGroup.get(k);
				ESLMessage networkMsg = new ESLMessage();
				ESLHeader header = new ESLHeader();
				header.setCode(HeaderType.REQ);
				header.setType(MessageType.NETWORKING);
				byte[] content = new byte[1];
				content[0] = 0x01;//开始组网
				header.setLength((byte) content.length);
				networkMsg.setContent(content);
				networkMsg.setEslHeader(header);
				ch.writeAndFlush(networkMsg);
				Timer timer = new Timer();//每个网关一个定时器
				timer.schedule(new NetworkTimeTask(ch), networkTimeout);
				timerMap.put(k,timer);
			});
		}

	}

	@OnEvent(value = "sendUpdate")
	public void onUpdateEvent(SocketIOClient client, AckRequest ackRequest, UpdateVO updatevo) {
		log.info("更新标签事件");
		Label label = labelService.findById(updatevo.getLabelId());
		Gateway gateway = null;
		if(label != null){
			gateway = label.getGateway();
			if(gateway != null){
				Map<String, SocketChannel> regChannelGroup = serverHandler.getRegChannelGroup();
				SocketChannel ch = regChannelGroup.get(gateway.getKey());
				ESLMessage controlMsg = new ESLMessage();
				ESLHeader header = new ESLHeader();
				header.setCode(HeaderType.REQ);
				header.setType(MessageType.UPDATE);
				controlMsg.setContent(ESLSocketUtils.convertUpdateToByte(updatevo).array());
				header.setLength((byte) NettyConstant.REQ_UPDATE_LENGTH);
				controlMsg.setEslHeader(header);
				ch.writeAndFlush(controlMsg);
			}else {
				client.sendEvent("sendControl","该标签没有入网");
			}
		}else{
			client.sendEvent("sendControl","没有找到对应的标签");
		}

	}

	public void toOne(String sessionId, String eventName, UpdateVO updateVO) {
		SocketIOClient client = clientMap.get(sessionId);
		if (client != null) {
			try {
				client.sendEvent(eventName, updateVO);
			} catch (Exception e) {
				log.error("推送消息给{}的客户端异常", sessionId, e.getMessage());
			}
		}
	}

	public void toOne(String sessionId, String eventName, Label label) {
		SocketIOClient client = clientMap.get(sessionId);
		if (client != null) {
			try {
				client.sendEvent(eventName, label);
			} catch (Exception e) {
				log.error("推送消息给{}的客户端异常", sessionId, e.getMessage());
			}
		}
	}

	public void toOneBoard(String sessionId, String eventName, OperatorVO message) {
		SocketIOClient client = clientMap.get(sessionId);
		if (client != null) {
			try {
				client.sendEvent(eventName, message);
			} catch (Exception e) {
				log.error("推送消息给{}的客户端异常", sessionId, e.getMessage());
			}
		}
	}

	public void toAll(UpdateVO updateVO) {
		for (String sessionId : clientMap.keySet()) {
			toOne(sessionId, "sendMessage", updateVO);
		}
	}

	public void toAll(Label label) {
		for (String sessionId : clientMap.keySet()) {
			toOne(sessionId, "sendMessage", label);
		}
	}

	public void toAllBoard(OperatorVO message) {
//		for (String sessionId : clientMap.keySet()) {
//			toOneBoard(sessionId, "sendBoard", message);
//		}
//		
		clientMap.forEach((k,v)->toOneBoard(k, "sendBoard", message));
	}

	public Map<String, Timer> getTimerMap() {
		return timerMap;
	}

	public void setTimerMap(Map<String, Timer> timerMap) {
		this.timerMap = timerMap;
	}

	@PreDestroy
	public void destory() {
		server.stop();
	}
}
