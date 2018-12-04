package com.boe.esl.websocketServer;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.boe.esl.model.Gateway;
import com.boe.esl.model.Goods;
import com.boe.esl.model.Label;
import com.boe.esl.model.Message;
import com.boe.esl.service.GatewayService;
import com.boe.esl.service.GoodsService;
import com.boe.esl.service.LabelService;
import com.boe.esl.socket.ESLSocketUtils;
import com.boe.esl.socket.NettyConstant;
import com.boe.esl.socket.ServerHandler;
import com.boe.esl.socket.struct.ESLHeader;
import com.boe.esl.socket.struct.ESLMessage;
import com.boe.esl.socket.struct.HeaderType;
import com.boe.esl.socket.struct.MessageType;
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
	private GoodsService goodsService;

	@Autowired
	private GatewayService gatewayService;

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

	@OnEvent(value = "sendBoard")
	public void onBoardEvent(SocketIOClient client, AckRequest ackRequest, Message message) {
		Map<String, SocketChannel> regChannelGroup = serverHandler.getRegChannelGroup();
		Set<String> keySet = regChannelGroup.keySet();
		Iterator<String> keyIt = keySet.iterator();
		while (keyIt.hasNext()) {
			String key = keyIt.next();
			SocketChannel channel = regChannelGroup.get(key);
			ESLMessage infoMsg = new ESLMessage();
			ESLHeader header = new ESLHeader();
			header.setCode(HeaderType.RESP);
			header.setType(MessageType.LOGIN);
			header.setLength((byte) 2);
			infoMsg.setEslHeader(header);
			byte[] content = new byte[2];
			content[0] = (byte)message.getBoardId();
			content[1] = Byte.parseByte(message.getBody());
			infoMsg.setContent(content);
			channel.writeAndFlush(infoMsg);

		}
		client.sendEvent("sendMessage", "server");
	}

	@OnEvent(value = "sendMessage")
	public void onEvent(SocketIOClient client, AckRequest ackRequest, UpdateVO updatevo) {
		log.info("接收客户端消息");
		Label label = labelService.findById(updatevo.getLabelId());
		if (label != null) {
			Goods goods = goodsService.findByLabelId(label.getId());
			Gateway gateway = label.getGateway();
			if (gateway != null && goods != null) {
				Map<String, SocketChannel> regChannelGroup = serverHandler.getRegChannelGroup();
				Set<String> keySet = regChannelGroup.keySet();
				Iterator<String> keyIt = keySet.iterator();
				while (keyIt.hasNext()) {
					String key = keyIt.next();
					if (key.equals(gateway.getKey())) {
						SocketChannel channel = regChannelGroup.get(key);
						ESLMessage infoMsg = new ESLMessage();
						ESLHeader header = new ESLHeader();
						header.setCode(HeaderType.REQ);
						header.setType(MessageType.INFO);
						header.setLength((byte) NettyConstant.REQ_INFO_LENGTH);
						infoMsg.setEslHeader(header);
						infoMsg.setContent(ESLSocketUtils.convertGoodsToByte(goods).array());
						channel.writeAndFlush(infoMsg);
					}

				}
			}
		}

		client.sendEvent("sendMessage", "server");

	}

	public void toOne(String sessionId, String eventName, UpdateVO updateVO) {
		SocketIOClient client = clientMap.get(sessionId);
		if (client != null) {
			try {
				client.sendEvent(eventName, updateVO);
				System.out.println(JsonConverter.objectToJSONObject(updateVO));
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
				System.out.println(JsonConverter.objectToJSONObject(message));
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
