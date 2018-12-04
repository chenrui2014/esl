package com.boe.esl.controller;

import lombok.ToString;

@ToString
public enum ResultEnum {

	RESOURCE_NOT_FOUND(404,"资源不存在"),
	SERVER_ERROR(500, "服务器异常"),
	DATA_ERROR(505,"数据库操作失败"),
	LOGIN_USER_NOT_EXIST_ERROR(601,"登录失败，用户不存在"),
	LOGIN_USERNAME_OR_PASSWORD_ERROR(602,"登录失败，用户名或者密码错误"),
	OK(200,"成功");
	
	private int code;
	private String msg;
	 ResultEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	 
	 
}
