package com.boe.esl.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName NettySocketIOConfig
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年8月15日 下午2:55:00
 * @lastUpdate 2018年8月15日 下午2:55:00
 */
@Configuration
@Slf4j
public class NettySocketIOConfig {
	
	@Resource
	private SocketIOProperties socketIOProperties;
	
	@Bean
	public SocketIOServer socketIOServer() {
		com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
		config.setPort(socketIOProperties.getSocketPort());
		config.setUpgradeTimeout(10000);
		config.setPingInterval(socketIOProperties.getPingInterval());
		config.setPingTimeout(socketIOProperties.getPingTimeout());
		config.setOrigin(null);
		config.setWorkerThreads(100);
		config.setAuthorizationListener(data ->{
			// 可以使用如下代码获取用户密码信息
            String token = data.getSingleUrlParam("token");
            log.info(token + "认证成功");
            return true;
		} );
		return new SocketIOServer(config);
	}
	@Bean
	public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
		return new SpringAnnotationScanner(socketServer);
	}
}
