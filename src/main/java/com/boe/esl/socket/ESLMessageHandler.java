package com.boe.esl.socket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.boe.esl.model.Goods;
import com.boe.esl.model.Label;
import com.boe.esl.socket.struct.ESLHeader;
import com.boe.esl.socket.struct.ESLMessage;
import com.boe.esl.socket.struct.HeaderType;
import com.boe.esl.socket.struct.MessageType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ESLMessageHandler extends ChannelInboundHandlerAdapter {

	private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();
	private String[] whitekList = {"127.0.0.1"};
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelUnregistered(ctx);
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("--------------------------------handler channelActive------------");
		ESLMessage eslMessage = new ESLMessage();
		ESLHeader header = new ESLHeader();
		header.setCode(HeaderType.REQ);
		header.setLength((byte)NettyConstant.REQ_INFO_LENGTH);
		header.setType(MessageType.REGISTER);
		eslMessage.setEslHeader(header);
		Goods goods = new Goods();
		Label label = new Label();
		label.setMac("30:9C:23:BF:21:6D");
		goods.setLabel(label);
		goods.setName("电脑");
		ByteBuf buf =ESLSocketUtils.convertGoodsToByte(goods);
		eslMessage.setContent(buf.array());
		ctx.writeAndFlush(eslMessage);
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ESLMessage requestMsg = (ESLMessage)msg;
		System.err.println("Server receive message from Client: " + requestMsg.getContent());
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}
	
}
