package com.boe.esl.websocketServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boe.esl.model.Message;
import com.boe.esl.utils.JsonConverter;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageEventHandler {

	private SocketIOServer server;
	
	private Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();
	
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
	
	@OnEvent(value="sendMessage")
	public void onEvent(SocketIOClient client, AckRequest ackRequest, Message message) {
		log.info("接收客户端消息");
		client.sendEvent("sendMessage", "server");
		
	}
	
	public void toOne(String sessionId, String eventName,Message message) {
		SocketIOClient client = clientMap.get(sessionId);
		if(client != null) {
			try {
				client.sendEvent(eventName, message);
				System.out.println(JsonConverter.objectToJSONObject(message));
			}catch (Exception e) {
				log.error("推送消息给{}的客户端异常",sessionId,e.getMessage());
			}
		}
	}
	
	public void toAll(Message message) {
		for(String sessionId:clientMap.keySet()) {
			toOne(sessionId, "sendMessage", message);
		}
	}
	@PreDestroy
    public void destory() {
    	server.stop();
    }
}
