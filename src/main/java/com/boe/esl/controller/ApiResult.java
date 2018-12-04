package com.boe.esl.controller;

import java.util.HashMap;

/**
 * 统一返回结果封装类
 * @ClassName ApiResult
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年9月11日 上午11:38:35
 * @lastUpdate 2018年9月11日 上午11:38:35
 */
@Deprecated
public class ApiResult extends HashMap<String, Object> {
	/**
	 * @field serialVersionUID:TODO()
	 */
	private static final long serialVersionUID = 1L;
	
	private static final ResultEnum OK_RESULT = ResultEnum.OK;
	private static final String CODE_KEY = "code";
	private static final String MSG_KEY = "msg";
	private static final String DATA_KEY = "data";
	
	private ApiResult() {
		put(CODE_KEY, OK_RESULT.getCode());
		put(MSG_KEY, OK_RESULT.getMsg());
	}

	public static ApiResult ok(Object data) {
		ApiResult result = new ApiResult();
		result.put(DATA_KEY, data);
		return result;
	}
	
	public static ApiResult ok(String msg) {
		ApiResult result = new ApiResult();
		result.put(MSG_KEY, msg);
		return result;
	}
	
	public static ApiResult ok(Object data, String msg) {
		ApiResult result = new ApiResult();
		result.put(MSG_KEY, msg);
		result.put(DATA_KEY, data);
		return result;
	}
	
	public static ApiResult ok() {
		ApiResult result = new ApiResult();
		return result;
	}
	
	public static ApiResult error(int code, String msg) {
		ApiResult result = new ApiResult();
		result.put(CODE_KEY, code);
		result.put(MSG_KEY, msg);
		return result;
	}
	
	public static ApiResult error(ResultEnum resultEnum) {
		ApiResult result = new ApiResult();
		result.put(CODE_KEY, resultEnum.getCode());
		result.put(MSG_KEY, resultEnum.getMsg());
		return result;
	}
}
