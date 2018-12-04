package com.boe.esl.socket;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;

@Component
@Qualifier("springProtocolInitializer")
public class ESLProtocolInitializer extends ChannelInitializer<SocketChannel> {

	@Autowired
	private ESLMessageDecoder eslDecoder;
	@Autowired
	private ESLMessageEncoder eslEncoder;
	@Autowired
	private ServerHandler serverHandler;
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("eslDecoder", new ESLMessageDecoder(1024*1024*5, 4, 1,0,0));
		pipeline.addLast("eslEncoder", new ESLMessageEncoder());
		pipeline.addLast(new IdleStateHandler(2000,0,0,TimeUnit.SECONDS));
		pipeline.addLast("serverHandler", serverHandler);
		
	}

	public ESLMessageDecoder getEslDecoder() {
		return eslDecoder;
	}

	public void setEslDecoder(ESLMessageDecoder eslDecoder) {
		this.eslDecoder = eslDecoder;
	}

	public ESLMessageEncoder getEslEncoder() {
		return eslEncoder;
	}

	public void setEslEncoder(ESLMessageEncoder eslEncoder) {
		this.eslEncoder = eslEncoder;
	}

	public ServerHandler getServerHandler() {
		return serverHandler;
	}

	public void setServerHandler(ServerHandler serverHandler) {
		this.serverHandler = serverHandler;
	}
	
}
