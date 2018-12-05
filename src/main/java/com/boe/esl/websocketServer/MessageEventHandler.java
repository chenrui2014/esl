package com.boe.esl.websocketServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import com.boe.esl.model.*;
import com.boe.esl.socket.struct.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	@Qualifier("serverHandler")
	private ServerHandler serverHandler;

	@Autowired
	public MessageEventHandler(SocketIOServer server) {
		this.server = server;
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
		Label label = labelService.getLabelByCode(controlMessage.getLabelCode());
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
				byte[] content = new byte[1];
				content[0] = 0x01;
				controlMsg.setContent(content);
				header.setLength((byte) content.length);
				controlMsg.setEslHeader(header);
				ch.writeAndFlush(controlMsg);
			}else{
				client.sendEvent("sendControl","该标签没有入网");
			}
		}else {
			client.sendEvent("sendControl","没有找到对应的标签");
		}
	}

	@OnEvent(value = "sendUpdate")
	public void onUpdateEvent(SocketIOClient client, AckRequest ackRequest, UpdateVO updatevo) {
		log.info("更新标签");
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

	@PreDestroy
	public void destory() {
		server.stop();
	}
}
