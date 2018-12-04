package com.boe.esl.socket.client;

import com.boe.esl.socket.ESLSocketUtils;
import com.boe.esl.socket.NettyConstant;
import com.boe.esl.socket.struct.ESLHeader;
import com.boe.esl.socket.struct.ESLMessage;
import com.boe.esl.socket.struct.HeaderType;
import com.boe.esl.socket.struct.MessageType;
import com.boe.esl.socket.struct.ResultStatus;
import com.boe.esl.utils.MessageDigestUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.err.println("通道激活");
		ESLMessage eslMessage = new ESLMessage();
		ESLHeader header = new ESLHeader();
		header.setCode(HeaderType.REQ);
		header.setLength((byte) NettyConstant.REQ_REGISTER_LENGTH);
		header.setType(MessageType.REGISTER);
		eslMessage.setEslHeader(header);
		byte[] content = new byte[NettyConstant.REQ_REGISTER_LENGTH];
		String hello = "g";
		try {
			System.arraycopy(hello.getBytes(), 0, content, 0, hello.getBytes().length);
			System.arraycopy(MessageDigestUtils.md5(hello).getBytes(), 0, content, 8,
					MessageDigestUtils.md5(hello).getBytes().length);
		} catch (Exception e) {
			log.error("error", e.getMessage());
		}

		eslMessage.setContent(content);
		// ByteBuf buf = Unpooled.copiedBuffer(hello.getBytes());
		ctx.writeAndFlush(eslMessage);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ESLMessage eslMessage = (ESLMessage) msg;
		if (eslMessage.getEslHeader().getType() == MessageType.REGISTER) {
			int count = 0;
			while(count < 6000) {
				ESLMessage reqMsg = new ESLMessage();
				ESLHeader header = new ESLHeader();
				header.setCode(HeaderType.REQ);
				header.setLength((byte) 2);
				header.setType(MessageType.LOGIN);
				reqMsg.setEslHeader(header);
				byte[] content = new byte[2];
				content[0] = 1;
				content[1] = 1;
				reqMsg.setContent(content);
				ctx.writeAndFlush(reqMsg);
				count++;
				Thread.sleep(10000);
			}
			
		} else if (eslMessage.getEslHeader().getType() == MessageType.LOGIN) {
			byte[] content = eslMessage.getContent();
			if (content.length >= 2) {
				int operId = content[0];
				int labelId = content[1];
				System.out.println("operId:" + operId + ",labelId:" + labelId);
			}

		} else if (eslMessage.getEslHeader().getType() == MessageType.INFO) {

			ESLMessage reqMsg = new ESLMessage();
			ESLHeader header = new ESLHeader();
			header.setCode(HeaderType.RESP);
			header.setLength((byte) 7);
			header.setType(MessageType.INFO);
			reqMsg.setEslHeader(header);
			byte[] content = new byte[7];
			content[0] = ResultStatus.OK.value();
			byte[] macBytes = ESLSocketUtils.macToByteArray("0a:33:24:62:5d:35");
			content[1] = 12;
			System.arraycopy(content, 1, macBytes, 0, 6);
			reqMsg.setContent(content);
			ctx.writeAndFlush(reqMsg);
		}
		// System.err.println("Client receive message from server: " +
		// eslMessage.getContent());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println("----------客户端数据读异常-----------");
		ctx.close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;

			if (event.state().equals(IdleState.ALL_IDLE)) {
				log.info("------超过10秒未给服务器发送消息或者未收到服务器反馈数据------");
				ESLMessage pingMessage = new ESLMessage();
				ESLHeader header = new ESLHeader();
				header.setCode(HeaderType.REQ);
				header.setType(MessageType.PING);
				header.setLength((byte) 0);
				pingMessage.setEslHeader(header);
				pingMessage.setContent(new byte[0]);
				ctx.writeAndFlush(pingMessage);
				// 根据具体的情况 在这里也可以重新连接
			}
		}
		super.userEventTriggered(ctx, evt);
	}

}
