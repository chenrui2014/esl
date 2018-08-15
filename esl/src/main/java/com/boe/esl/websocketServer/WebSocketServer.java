//package com.boe.esl.websocketServer;
//
//import com.boe.esl.model.LoginRequest;
//import com.corundumstudio.socketio.AckRequest;
//import com.corundumstudio.socketio.AuthorizationListener;
//import com.corundumstudio.socketio.Configuration;
//import com.corundumstudio.socketio.HandshakeData;
//import com.corundumstudio.socketio.SocketIOClient;
//import com.corundumstudio.socketio.SocketIOServer;
//import com.corundumstudio.socketio.listener.ConnectListener;
//import com.corundumstudio.socketio.listener.DataListener;
//
//import ch.qos.logback.classic.Logger;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class WebSocketServer {
//
//	public static void main(String[] args) {
//		Configuration config = new Configuration();
//		config.setHostname("localhost");
//		config.setPort(9092);
//		config.setUpgradeTimeout(10000);//协议升级超时时间
//		config.setPingTimeout(180000);//Ping消息超时时间
//		config.setPingInterval(60000);//Ping消息间隔时间
//		config.setAuthorizationListener(new AuthorizationListener() {
//			
//			@Override
//			public boolean isAuthorized(HandshakeData data) {
//				// TODO Auto-generated method stub
//				return true;
//			}
//		});
//		final SocketIOServer server = new SocketIOServer(config);
//		server.addConnectListener(new ConnectListener() {
//			
//			@Override
//			public void onConnect(SocketIOClient client) {
//				if(client != null) {
//					log.info("连接成功。clientId="+client.getSessionId().toString());
//					client.joinRoom(client.getHandshakeData().getSingleUrlParam("appid"));
//				}else {
//					log.info("并没有人连接上");
//				}
//			}
//		});
//		server.addEventListener("login", LoginRequest.class, new DataListener<LoginRequest>() {
//
//			@Override
//			public void onData(SocketIOClient client, LoginRequest data, AckRequest ackSender) throws Exception {
//				log.info("接收到客户端消息");
//				if(ackSender.isAckRequested()){
//					
//				}
//			}
//			
//		});
//	}
//}
