package com.boe.esl.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class GatewayVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	private String key;
	private boolean online;
	private String status;

}
