//package com.boe.esl.socket;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Component
//@Order(1)
//@Slf4j
//public class SocketServerRunner implements CommandLineRunner {
//
//	private EslSocketServer eslServer;
//	
//	public SocketServerRunner(EslSocketServer eslServer) {
//
//		this.eslServer = eslServer;
//	}
//	@Override
//	public void run(String... args) throws Exception {
//
//		log.info("ESLSocketServer启动");
//		eslServer.run();
//	}
//
//}
