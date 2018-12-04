package com.boe.esl.vo;

import lombok.Data;

@Data
public class OrderVO implements BaseVO {

	/**
	 * @field serialVersionUID:TODO()
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String code;
	private String orderTime;
	private int count;
	private long goodsId;
	private String goodsName;
	private String status;
	private String statusName;
	
}
