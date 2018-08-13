package com.boe.esl.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class UpdateVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private long labelId;
	private String labelName;
	private String labelMac;
	private long goodsId;
	private String goodsName;
	private String updateTime;
	private String statusName;
	private short status;
}
