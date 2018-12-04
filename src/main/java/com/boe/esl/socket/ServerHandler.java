package com.boe.esl.socket;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
import com.boe.esl.socket.struct.ESLHeader;
import com.boe.esl.socket.struct.ESLMessage;
import com.boe.esl.socket.struct.HeaderType;
import com.boe.esl.socket.struct.MessageType;
import com.boe.esl.socket.struct.ResultStatus;
import com.boe.esl.websocketServer.MessageEventHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;

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
			case REGISTER:
				handleRegister((SocketChannel) ctx.channel(), eslMessage);
				break;
			case AWAKE:// 唤醒成功
				byte[] content = eslMessage.getContent();
				SocketChannel ch = (SocketChannel) ctx.channel();
				if (content[0] == ResultStatus.OK.value()) {
					Set<String> keySet = regChannelGroup.keySet();
					Iterator<String> keyIt = keySet.iterator();
					while (keyIt.hasNext()) {
						String key = keyIt.next();
						SocketChannel channel = regChannelGroup.get(key);
						if (ch.equals(channel)) {
							awaitChanellGroup.put(key, channel);
						}
					}
					sendInfo();// 发送Info命令
				}
				break;
			case LOGIN:
				handleLogin((SocketChannel) ctx.channel(), eslMessage);
				break;
			case INFO:
				handleInfo((SocketChannel) ctx.channel(), eslMessage);
				break;
			case UPDATE:
				handleUpdate((SocketChannel) ctx.channel(), eslMessage);
				break;
			case CANCEL:
				handleCancel((SocketChannel) ctx.channel(), eslMessage);
				break;
			case DONE:
				handleDone((SocketChannel) ctx.channel(), eslMessage);
				break;
			case BOM:
				handleBOM((SocketChannel) ctx.channel(), eslMessage);
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
				header.setType(MessageType.PING);
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
	 * 发送info命令
	 * 
	 * @Title sendInfo
	 * @Description TODO()
	 * @params
	 * @return void
	 * @throws @create 2018年8月30日 上午11:07:19
	 * @lastUpdate 2018年8月30日 上午11:07:19
	 */
	public void sendInfo() {
		Set<String> keySet = updateGoods.keySet();
		Iterator<String> keyIt = keySet.iterator();
		while (keyIt.hasNext()) {
			String key = keyIt.next();
			List<Goods> value = updateGoods.get(key);
			SocketChannel channel = regChannelGroup.get(key);
			if (channel != null && value != null && value.size() > 0) {
				for (Goods goods : value) {
					ESLMessage infoMsg = new ESLMessage();
					ESLHeader header = new ESLHeader();
					header.setCode(HeaderType.REQ);
					header.setType(MessageType.INFO);
					header.setLength((byte) NettyConstant.REQ_INFO_LENGTH);
					infoMsg.setEslHeader(header);
					infoMsg.setContent(ESLSocketUtils.convertGoodsToByte(goods).array());
					channel.writeAndFlush(infoMsg);
				}

				ESLMessage updateMsg = new ESLMessage();
				ESLHeader header = new ESLHeader();
				header.setCode(HeaderType.REQ);
				header.setType(MessageType.UPDATE);
				header.setLength((byte) 0);
				updateMsg.setEslHeader(header);
				updateMsg.setContent(new byte[0]);
				channel.writeAndFlush(updateMsg);
			}
		}
	}

	public void handleUpdate(SocketChannel ch, ESLMessage eslMessage) {
		byte[] content = eslMessage.getContent();
		if (content != null && content.length >= 1 && content[0] == ResultStatus.OK.value()) {

		}

	}

	/**
	 * 处理info响应
	 * 
	 * @Title handleInfo
	 * @Description TODO()
	 * @params @param ch
	 * @params @param eslMessage
	 * @return void
	 * @throws @create 2018年8月30日 上午11:07:37
	 * @lastUpdate 2018年8月30日 上午11:07:37
	 */
	public void handleInfo(SocketChannel ch, ESLMessage eslMessage) {
		byte[] content = eslMessage.getContent();
		if (content != null && content.length >= 7 && content[0] == ResultStatus.OK.value()) {
			byte[] macBytes = new byte[6];
			System.arraycopy(content, 1, macBytes, 0, 6);
			String mac = ESLSocketUtils.ByteArrayToMac(macBytes);
			Set<String> keySet = regChannelGroup.keySet();
			Iterator<String> keyIt = keySet.iterator();
			while (keyIt.hasNext()) {
				String key = keyIt.next();
				SocketChannel channel = regChannelGroup.get(key);
				if (channel != null && channel.equals(ch)) {
					List<Goods> goodsList = updateGoods.get(key);
					if (goodsList != null && goodsList.size() > 0) {
						Iterator<Goods> iterator = goodsList.iterator();
						while (iterator.hasNext()) {
							Goods goods = iterator.next();
							if (goods != null) {
								Label label = goods.getLabel();
								if (label != null && mac.equals(label.getMac())) {
									// Message message = new Message(mac);
									List<Update> updateList = label.getUpdates();
									if (updateList != null && updateList.size() > 0) {
										websocketHandler.toAll(updateService.convertEntity(updateList.get(0)));
									}

									goodsList.remove(goods);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 处理login请求
	 * 
	 * @Title handleLogin
	 * @Description TODO()
	 * @params @param ch
	 * @params @param eslMessage
	 * @return void
	 * @throws @create 2018年8月30日 上午11:07:53
	 * @lastUpdate 2018年8月30日 上午11:07:53
	 */
	public void handleLogin(SocketChannel ch, ESLMessage eslMessage) {
		byte[] content = eslMessage.getContent();
		if (content.length >= 2) {
			if (content.length == 2) {
				int boarId = content[0];
				int operatorId = content[1];
				Operator operator = operatorService.findById((long) operatorId);
				Board board = boardService.findById((long)boarId);
				if (operator != null) {
					Date date = new Date();
					Timestamp now = new Timestamp(date.getTime());
					operator.setLoginTime(now);
					operator.setStatus((short) 1);
					if (board != null) {
						operator.setBoard(board);
					}
					Operator operator2 = null;
					try {
						operator2 = operatorService.save(operator);

						if (operator2 != null) {
							websocketHandler.toAllBoard(operatorService.convertEntity(operator2));
						}

					} catch (Exception e) {
						log.error("保存操作员上线信息失败", e.getMessage());
					}
//				Operation operation = operator.getOperation();
//				if (operation != null) {
//					ESLMessage respMsg = new ESLMessage();
//					ESLHeader header = new ESLHeader();
//					header.setCode(HeaderType.RESP);
//					long operationId = operation.getId();
//					byte[] respCon = new byte[2];
//					respCon[0] = (byte) labelId;
//					respCon[1] = (byte) operationId;
//					Message message = new Message();
//					message.setBoardId(labelId);
//					message.setBody(operationId + "");
//					websocketHandler.toAllBoard(message);
//					respMsg.setContent(respCon);
//					header.setLength((byte) 2);
//					header.setType(MessageType.LOGIN);
//					respMsg.setEslHeader(header);
//					//ch.writeAndFlush(respMsg);
//				} else {
//					ESLMessage errMsg = new ESLMessage();
//					ESLHeader header = new ESLHeader();
//					header.setCode(HeaderType.RESP);
//					header.setType(MessageType.DONE);
//					header.setLength((byte) 1);
//					eslMessage.setEslHeader(header);
//					byte[] cont = new byte[1];
//					cont[0] = ResultStatus.SERVER_ERROR.value();
//					eslMessage.setContent(cont);
//					ch.writeAndFlush(errMsg);
//				}
				} else {
					ESLMessage errMsg = new ESLMessage();
					ESLHeader header = new ESLHeader();
					header.setCode(HeaderType.RESP);
					header.setType(MessageType.DONE);
					header.setLength((byte) 1);
					eslMessage.setEslHeader(header);
					byte[] cont = new byte[1];
					cont[0] = ResultStatus.SERVER_ERROR.value();
					eslMessage.setContent(cont);
					ch.writeAndFlush(errMsg);
				}
			}

		} else {
			ESLMessage errMsg = new ESLMessage();
			ESLHeader header = new ESLHeader();
			header.setCode(HeaderType.RESP);
			header.setType(MessageType.DONE);
			header.setLength((byte) 1);
			eslMessage.setEslHeader(header);
			byte[] cont = new byte[1];
			cont[0] = ResultStatus.AP_ERROR.value();
			eslMessage.setContent(cont);
			ch.writeAndFlush(errMsg);
		}
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
		sendAwake(channel);
	}

	/**
	 * 唤醒类消息
	 * 
	 * @Title awake
	 * @Description TODO()
	 * @params @return
	 * @return ESLMessage
	 * @throws @create 2018年8月24日 下午3:07:00
	 * @lastUpdate 2018年8月24日 下午3:07:00
	 */
	private void sendAwake(SocketChannel channel) {
		ESLMessage awakeMsg = new ESLMessage();
		ESLHeader header = new ESLHeader();
		byte[] content = new byte[0];
		header.setCode(HeaderType.REQ);
		header.setType(MessageType.AWAKE);
		header.setLength((byte) content.length);
		awakeMsg.setEslHeader(header);
		awakeMsg.setContent(content);
		channel.writeAndFlush(awakeMsg);
	}

	/**
	 * 取消更新类消息
	 * 
	 * @Title cancel
	 * @Description TODO()
	 * @params @param eslMessage
	 * @params @return
	 * @return ESLMessage
	 * @throws @create 2018年8月24日 下午1:54:56
	 * @lastUpdate 2018年8月24日 下午1:54:56
	 */
	private void sendCancel(SocketChannel channel) {
		ESLMessage respMessage = new ESLMessage();

	}

	private void handleCancel(SocketChannel channel, ESLMessage eslMessage) {

	}

	/**
	 * 更新成功类消息
	 * 
	 * @Title done
	 * @Description TODO()
	 * @params @param eslMessage
	 * @params @return
	 * @return ESLMessage
	 * @throws @create 2018年8月24日 下午1:55:11
	 * @lastUpdate 2018年8月24日 下午1:55:11
	 */
	private void sendDone(SocketChannel channel, ESLMessage eslMessage) {
		ESLMessage respMessage = new ESLMessage();

	}

	private void handleDone(SocketChannel channel, ESLMessage eslMessage) {

	}

	private void handleBOM(SocketChannel channel, ESLMessage eslMessage) {

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
