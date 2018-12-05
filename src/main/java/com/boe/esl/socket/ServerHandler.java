package com.boe.esl.socket;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.boe.esl.socket.struct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.boe.esl.model.Board;
import com.boe.esl.model.Gateway;
import com.boe.esl.model.Goods;
import com.boe.esl.model.Label;
import com.boe.esl.model.Operator;
import com.boe.esl.model.Update;
import com.boe.esl.service.BoardService;
import com.boe.esl.service.GatewayService;
import com.boe.esl.service.OperatorService;
import com.boe.esl.service.UpdateService;
import com.boe.esl.websocketServer.MessageEventHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;

import static com.boe.esl.socket.struct.DeviceType.*;

@Slf4j
@Component
@Qualifier("serverHandler")
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

	@Autowired
	private GatewayService gatewayService;

	@Autowired
	private UpdateService updateService;

	@Autowired
	private BoardService boardService;

	@Autowired
	private OperatorService operatorService;

	@Autowired
	private MessageEventHandler websocketHandler;

	// 成功注册的channel
	private Map<String, SocketChannel> regChannelGroup = new ConcurrentHashMap<String, SocketChannel>();
	// 成功唤醒的channel
	private Map<String, SocketChannel> awaitChanellGroup = new ConcurrentHashMap<String, SocketChannel>();

	private Map<String, List<Goods>> updateGoods = new ConcurrentHashMap<String, List<Goods>>();

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		ESLMessage eslMessage = (ESLMessage) msg;
		ESLHeader header = eslMessage.getEslHeader();
		if (header != null) {
			switch (header.getType()) {
			case REGISTER://注册
				handleRegister((SocketChannel) ctx.channel(), eslMessage);
				break;
			case NETWORKING://组网
				break;
			case PONG://心跳
				pong((SocketChannel) ctx.channel(), eslMessage);
				break;
			case NETWORK://新设备入网
				network((SocketChannel) ctx.channel(), eslMessage);
				break;
			case UPDATE://状态更新
				update((SocketChannel) ctx.channel(), eslMessage);
				break;
			case CONTROLLE://控制命令
				controlle((SocketChannel) ctx.channel(), eslMessage);
				break;
			case DISPLAY://显示更新
				display((SocketChannel) ctx.channel(), eslMessage);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("\nChannel is disconnected");
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		if (evt instanceof IdleStateEvent) {
			IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

			if (idleStateEvent.state() == IdleState.READER_IDLE) {
				log.info("已经20秒没有收到信息！");
				log.info("向客户端发送ping消息");
				// 向客户端发送消息
				ESLMessage eslMessage = new ESLMessage();
				ESLHeader header = new ESLHeader();
				header.setCode(HeaderType.REQ);
				header.setLength((byte) 0);
				eslMessage.setEslHeader(header);
				eslMessage.setContent(new byte[0]);
				ctx.writeAndFlush(eslMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
			}

		}

		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.handlerAdded(ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.handlerRemoved(ctx);
	}

	/**
	 * 处理注册类消息
	 * 
	 * @Title register
	 * @Description TODO()
	 * @params @param eslMessage
	 * @params @return
	 * @return ESLMessage
	 * @throws @create 2018年8月24日 下午1:53:18
	 * @lastUpdate 2018年8月24日 下午1:53:18
	 */
	private void handleRegister(SocketChannel channel, ESLMessage eslMessage) {
		byte[] gatewayBytes = new byte[8];
		System.arraycopy(eslMessage.getContent(), 0, gatewayBytes, 0, 8);
		String gatewayName = null;
		byte[] keyBytes = new byte[32];
		System.arraycopy(eslMessage.getContent(), 8, keyBytes, 0, 32);
		String key = null;
		gatewayName = new String(gatewayBytes);
		key = new String(keyBytes);
		gatewayName = gatewayName.trim();
		key = key.trim();
		Gateway gateway = new Gateway();
		gateway.setName(gatewayName);
		gateway.setKey(key);
		//gateway.setOnline(true);
		Gateway gateway2 = gatewayService.findByNameAndKey(gateway);
		if (gateway2 != null) {
			gateway = gateway2;
		}

		ESLMessage respMessage = new ESLMessage();
		ESLHeader header = new ESLHeader();
		header.setCode(HeaderType.RESP);
		header.setType(MessageType.REGISTER);
		header.setLength((byte) 1);
		respMessage.setEslHeader(header);
		try {
			gatewayService.save(gateway);
			byte[] status = { ResultStatus.OK.value() };
			respMessage.setContent(status);

			regChannelGroup.put(gatewayName, channel);// 保存通道

		} catch (Exception e) {

			byte[] status = { ResultStatus.SERVER_ERROR.value() };
			respMessage.setContent(status);
		}

		channel.writeAndFlush(respMessage);
	}

	/**
	 * 组网
	 * @param channel
	 */
	private void networking(SocketChannel channel){
		ESLMessage networkingMsg = new ESLMessage();
		ESLHeader header = new ESLHeader();
		byte[] content = new byte[1];
		content[0] = 0x01;//开始组网
		header.setCode(HeaderType.REQ);
		header.setType(MessageType.NETWORKING);
		header.setLength((byte) content.length);
		networkingMsg.setEslHeader(header);
		networkingMsg.setContent(content);
		channel.writeAndFlush(networkingMsg);
	}

	/**
	 * 响应心跳
	 * @param channel
	 */
	private void pong(SocketChannel channel, ESLMessage eslMessage){
		byte[] gatewayBytes = new byte[8];
		System.arraycopy(eslMessage.getContent(), 0, gatewayBytes, 0, 8);
		String gatewayName = null;
		gatewayName = new String(gatewayBytes);
		gatewayName = gatewayName.trim();
		SocketChannel ch = regChannelGroup.get(gatewayName);
		ESLMessage pongMsg = new ESLMessage();
		ESLHeader header = new ESLHeader();
		header.setCode(HeaderType.RESP);
		header.setType(MessageType.PONG);
		byte[] content = new byte[0];
		pongMsg.setContent(content);
		header.setLength((byte)content.length);
		pongMsg.setEslHeader(header);

		ch.writeAndFlush(pongMsg);
	}

	/**
	 * 新设备入网
	 * @param channel
	 * @param eslMessage
	 */
	private void network(SocketChannel channel, ESLMessage eslMessage) {
		byte[] deviceTypeBytes = new byte[1];
		System.arraycopy(eslMessage.getContent(), 0, deviceTypeBytes, 0, 1);

		if(deviceTypeBytes.length > 0){
			switch (deviceTypeBytes[0]) {
				case 0x01:
					break;
				case 0x02:
					break;
				case 0x03:
					break;
				case 0x04:
					break;
				default:
					break;
			}
		}

		byte[] deviceIDBytes = new byte[2];
		System.arraycopy(eslMessage.getContent(), 0, deviceIDBytes, 0, 8);
		String deviceID = null;
		deviceID = new String(deviceIDBytes);
		deviceID = deviceID.trim();
		ESLMessage networkMsg = new ESLMessage();
		ESLHeader header = new ESLHeader();
		header.setCode(HeaderType.RESP);
		header.setType(MessageType.NETWORK);
		byte[] content = new byte[0];
		networkMsg.setContent(content);
		header.setLength((byte)content.length);
		networkMsg.setEslHeader(header);

		channel.writeAndFlush(networkMsg);

	}

	/**
	 * 控制命令
	 * @param channel
	 * @param eslMessage
	 */
	private void controlle(SocketChannel channel, ESLMessage eslMessage) {
		ESLMessage respMessage = new ESLMessage();

	}

	/**
	 * 设备状态更新
	 * @param channel
	 * @param eslMessage
	 */
	private void update(SocketChannel channel, ESLMessage eslMessage) {

	}

	/**
	 * 显示更新
	 * @param channel
	 * @param eslMessage
	 */
	private void display(SocketChannel channel, ESLMessage eslMessage) {

	}

	public GatewayService getGatewayService() {
		return gatewayService;
	}

	public void setGatewayService(GatewayService gatewayService) {
		this.gatewayService = gatewayService;
	}

	public MessageEventHandler getWebsocketHandler() {
		return websocketHandler;
	}

	public void setWebsocketHandler(MessageEventHandler websocketHandler) {
		this.websocketHandler = websocketHandler;
	}

	public Map<String, SocketChannel> getRegChannelGroup() {
		return regChannelGroup;
	}

	public void setRegChannelGroup(Map<String, SocketChannel> regChannelGroup) {
		this.regChannelGroup = regChannelGroup;
	}

	public Map<String, SocketChannel> getAwaitChanellGroup() {
		return awaitChanellGroup;
	}

	public void setAwaitChanellGroup(Map<String, SocketChannel> awaitChanellGroup) {
		this.awaitChanellGroup = awaitChanellGroup;
	}
}
