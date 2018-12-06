package com.boe.esl.vo;

import lombok.Data;

@Data
public class LabelVO implements BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String mac;
	private Long gatewayId;
	private String gatewayMac;
	private String code;
	private String type;
	private Integer status;
	private String statusText;
	private String power;


}
