package com.boe.esl.controller;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * 统一返回结果类
 * @ClassName RestResult
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年9月11日 下午4:27:47
 * @lastUpdate 2018年9月11日 下午4:27:47
 * @param <T>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResult<T> implements Serializable {

	/**
	 * @field serialVersionUID:TODO()
	 */
	private static final long serialVersionUID = 1L;

	protected int code;
	protected String msg;
	protected T data;
	protected boolean success;
	
	protected RestResult() {}

    public static <T> RestResult<T> newInstance() {
        return new RestResult<>();
    }
}
