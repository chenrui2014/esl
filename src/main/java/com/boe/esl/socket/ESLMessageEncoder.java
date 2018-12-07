package com.boe.esl.socket;

import com.boe.esl.socket.struct.ESLMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ESLMessageEncoder extends MessageToByteEncoder<ESLMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ESLMessage msg, ByteBuf out) throws Exception {

		if(msg == null || msg.getEslHeader() == null) {
			throw new Exception("没有需要编码的消息");
		}
		out.writeByte(msg.getEslHeader().getCode().value());
		out.writeByte(msg.getEslHeader().getVersion());
		out.writeByte(msg.getEslHeader().getType().value());
		out.writeByte(msg.getEslHeader().getPadding());
		out.writeByte(msg.getContent().length);
		out.writeBytes(msg.getContent());
	}

}
