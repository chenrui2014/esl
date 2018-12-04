package com.boe.esl.socket.client;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;

import com.boe.esl.model.Goods;
import com.boe.esl.model.Label;
import com.boe.esl.socket.ESLMessageDecoder;
import com.boe.esl.socket.ESLMessageEncoder;
import com.boe.esl.socket.ESLSocketUtils;
import com.boe.esl.socket.struct.ESLHeader;
import com.boe.esl.socket.struct.ESLMessage;
import com.boe.esl.socket.struct.HeaderType;
import com.boe.esl.socket.struct.MessageType;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class EslSocketClient {

//
//	@Value("${socket.server-ip}")
//	private String remoteIp;
//	@Value("${socket.server-port}")
//	private int port;

	public static void main(String args[]) throws InterruptedException {

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.option(ChannelOption.SO_BACKLOG, 100)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ESLMessageDecoder(1024*1024*5, 4, 1,0,0));
					ch.pipeline().addLast(new ESLMessageEncoder());
					ch.pipeline().addLast(new IdleStateHandler(0, 0, 1000, TimeUnit.SECONDS));
					ch.pipeline().addLast(new ClientHandler());
				}
			});

			// 发起异步连接操作
			ChannelFuture future = b.connect(new InetSocketAddress("127.0.0.1", 9092),
					new InetSocketAddress("127.0.0.1", 9095)).sync();
//			Channel channel = future.channel();
			
//			ESLMessage reqMsg = new ESLMessage();
//			ESLHeader header = new ESLHeader();
//			header.setCode(HeaderType.REQ);
//			header.setLength((byte)2);
//			header.setType(MessageType.LOGIN);
//			reqMsg.setEslHeader(header);
//			byte[] cc = new byte[2];
//			cc[0]=10;
//			cc[1]=12;
//			reqMsg.setContent(cc);
			
			//channel.read();
			future.channel().closeFuture().sync();

		} finally {
			group.shutdownGracefully();
		}
	}
}
