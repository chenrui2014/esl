package com.boe.esl.websocketServer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;

import lombok.extern.slf4j.Slf4j;

/**
 * SpringBoot启动之后执行
 * @ClassName ServerRunner
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年8月15日 下午4:04:03
 * @lastUpdate 2018年8月15日 下午4:04:03
 */
@Component
@Order(1)
@Slf4j
public class ServerRunner implements CommandLineRunner {

	private SocketIOServer server;
	
	public ServerRunner(SocketIOServer server) {
        this.server = server;
    }
	
	@Override
	public void run(String... args) throws Exception {
		log.info("websocket开始启动了");
		server.start();
	}

}
