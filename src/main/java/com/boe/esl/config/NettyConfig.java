package com.boe.esl.config;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.boe.esl.socket.ESLMessageDecoder;
import com.boe.esl.socket.ESLMessageEncoder;
import com.boe.esl.socket.ESLProtocolInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *  Netty 网关服务器端配置
 * @ClassName NettyConfig
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年9月14日 下午1:27:45
 * @lastUpdate 2018年9月14日 下午1:27:45
 */
@Configuration
public class NettyConfig {

	@Value("${socket.boss.thread-num}")
	private int bossNum;
	@Value("${socket.worker.thread-num}")
	private int workerNum;
	@Value("${socket.server-ip}")
	private String remoteIp;
	@Value("${socket.server-port}")
	private int port;
	@Value("${socket.so.keepalive}")
	private boolean keepAlive;
	@Value("${socket.so.backlog}")
	private int backlog;

	@Autowired
	@Qualifier("springProtocolInitializer")
	private ESLProtocolInitializer eslProtocolInitializer;

	@SuppressWarnings("unchecked")
	@Bean(name = "serverBootstrap")
	public ServerBootstrap bootstrap() {
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup(), workerGroup()).channel(NioServerSocketChannel.class).childHandler(eslProtocolInitializer);
		Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
		Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
		for (@SuppressWarnings("rawtypes")
		ChannelOption option : keySet) {
			b.option(option, tcpChannelOptions.get(option));
		}
		return b;
	}

	@Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup bossGroup() {
		return new NioEventLoopGroup(bossNum);
	}

	@Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup workerGroup() {
		return new NioEventLoopGroup(workerNum);
	}

	@Bean(name = "tcpChannelOptions")
	public Map<ChannelOption<?>, Object> tcpChannelOptions() {
		Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
		options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
		options.put(ChannelOption.SO_BACKLOG, backlog);
		return options;
	}
	
	@Bean(name = "tcpServerAddress")
	public InetSocketAddress tcpAddress() {
		return new InetSocketAddress(remoteIp, port);
	}
	
	@Bean(name="eslEncoder")
	public ESLMessageEncoder eslEncoder() {
		return new ESLMessageEncoder();
	}
	
	@Bean(name="eslDecoder")
	public ESLMessageDecoder eslDecoder() {
		return new ESLMessageDecoder(1024*1024*5, 4, 1,0,0);
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
