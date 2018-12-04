package com.boe.esl.socket;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;

@Component
public class ESLServer {

	@Autowired
    @Qualifier("serverBootstrap")
    private ServerBootstrap b;
	
	@Autowired
    @Qualifier("tcpServerAddress")
    private InetSocketAddress tcpAddress;
	
	private ChannelFuture serverChannelFuture;
	
	@PostConstruct
    public void start() throws Exception {
		System.out.println("Starting server at " + tcpAddress);
        serverChannelFuture = b.bind(tcpAddress).sync();
	}
	
	@PreDestroy
    public void stop() throws Exception {
		serverChannelFuture.channel().closeFuture().sync();
	}

	public ServerBootstrap getB() {
		return b;
	}

	public void setB(ServerBootstrap b) {
		this.b = b;
	}

	public InetSocketAddress getTcpAddress() {
		return tcpAddress;
	}

	public void setTcpAddress(InetSocketAddress tcpAddress) {
		this.tcpAddress = tcpAddress;
	}

	public ChannelFuture getServerChannelFuture() {
		return serverChannelFuture;
	}

	public void setServerChannelFuture(ChannelFuture serverChannelFuture) {
		this.serverChannelFuture = serverChannelFuture;
	}
	
}
