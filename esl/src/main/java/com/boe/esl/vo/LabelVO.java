package com.boe.esl.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class LabelVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String mac;
	private long gatewayId;
	private String gatewayName;
	private String shelf;
	private short status;
	private String statusName;


}
