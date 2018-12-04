package com.boe.esl.vo;

import lombok.Data;

@Data
public class UserVO implements BaseVO {

	/**
	 * @field serialVersionUID:TODO()
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String userName;
	private String password;
	private String email;
	private Boolean status;
	private String statusString;
}
