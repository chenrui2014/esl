package com.boe.esl.vo;

import lombok.Data;

@Data
public class GoodsVO implements BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	private double price;
	private String unit;
	private long labelId;
	private String labelName;
	private String labelMac;

}
