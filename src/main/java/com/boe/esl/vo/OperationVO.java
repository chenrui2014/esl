package com.boe.esl.vo;

import lombok.Data;

@Data
public class OperationVO implements BaseVO {

	/**
	 * @field serialVersionUID:TODO()
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String code;
	private String content;
}
