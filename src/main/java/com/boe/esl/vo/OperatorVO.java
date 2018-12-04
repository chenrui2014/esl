package com.boe.esl.vo;

import lombok.Data;

@Data
public class OperatorVO implements BaseVO {

	/**
	 * @field serialVersionUID:TODO()
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	private String code;
	private String type;
	private String loginTime;
	private String statusString;
	private short status;
	private long operationId;
	private String operationName;
	private String operationCode;
	private long boardId;
	private String boardName;
}
