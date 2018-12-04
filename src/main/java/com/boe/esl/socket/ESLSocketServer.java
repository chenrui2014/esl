package com.boe.esl.socket;
//package com.boe.esl.socket;
//
//import javax.annotation.PreDestroy;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import lombok.extern.slf4j.Slf4j;
//
//@Component
//@Slf4j
//public class EslSocketServer {
//
//	/**
//	 * 用于分配处理业务线程的线程组个数
//	 */
//	protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2; // 默认
//
//	/**
//	 * 业务出现线程大小
//	 */
//	protected static final int BIZTHREADSIZE = 4;
//
//	/**
//	 * NioEventLoopGroup实际上就是个线程池,
//	 * NioEventLoopGroup在后台启动了n个NioEventLoop来处理Channel事件,
//	 * 每一个NioEventLoop负责处理m个Channel,
//	 * NioEventLoopGroup从NioEventLoop数组里挨个取出NioEventLoop来处理Channel
//	 */
//	private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
//	private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);
//
//	@Value("${socket.server-ip}")
//	private String remoteIp;
//	@Value("${socket.server-port}")
//	private int port;
//
//	protected void run() throws Exception {
//		ServerBootstrap b = new ServerBootstrap();
//		b.group(bossGroup, workerGroup)
//		.channel(NioServerSocketChannel.class)
//		.option(ChannelOption.SO_BACKLOG, 1024) // 设置TCP缓冲区
//		.option(ChannelOption.SO_RCVBUF, 32 * 1024) // 设置接受数据的缓存大小
//		.childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE) // 设置保持连接
//		.childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
//		.childHandler(new ChannelInitializer<SocketChannel>() {
//
//					@Override
//					protected void initChannel(SocketChannel ch) throws Exception {
//
//						ChannelPipeline pipeline = ch.pipeline();
//						pipeline.addLast("ESLDecoder", new ESLMessageDecoder(1024*1024*5, 4, 4))
//								.addLast("ESLEncoder", new ESLMessageEncoder())
//								.addLast(new ESLMessageHandler());
//					}
//
//				});
//
//		ChannelFuture f = b.bind(this.remoteIp, this.port).sync();
//
//		log.info("ESLSocket服务器已启动");
//
//		f.channel().closeFuture().sync();
//
//		shutdown();
//	}
//
//	protected static void shutdown() {
//		workerGroup.shutdownGracefully();
//		bossGroup.shutdownGracefully();
//	}
//}
