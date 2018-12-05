package com.boe.esl.socket.client;

import com.boe.esl.socket.NettyConstant;
import com.boe.esl.socket.struct.ESLHeader;
import com.boe.esl.socket.struct.ESLMessage;
import com.boe.esl.socket.struct.HeaderType;
import com.boe.esl.socket.struct.MessageType;
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
				//header.setType(MessageType.PING);
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
