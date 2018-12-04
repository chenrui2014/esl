package com.boe.esl.vo;

import lombok.Data;

@Data
public class Card implements BaseVO {

	/**
	 * @field serialVersionUID:TODO()
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String title;
	private long operatorId;
	private String message;
	
}
