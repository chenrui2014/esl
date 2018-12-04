package com.boe.esl.vo;

import lombok.Data;

@Data
public class GatewayVO implements BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String key;
	private Short status;
	private String statusText;
	private String mac;
	private String ip;

}
