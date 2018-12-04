package com.boe.esl.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@ConfigurationProperties(prefix="esl")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SocketIOProperties {

	private Integer socketPort;
	private Integer pingInterval;
	private Integer pingTimeout;
}
