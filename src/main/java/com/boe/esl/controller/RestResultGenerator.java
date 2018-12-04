package com.boe.esl.controller;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * 返回结果生成器
 * @ClassName RestResultGenerator
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年9月11日 下午4:28:25
 * @lastUpdate 2018年9月11日 下午4:28:25
 */
@Slf4j
public class RestResultGenerator {

	/**
	 * 生成返回结果
	 * @Title genResult
	 * @Description TODO()
	 * @param data
	 * @param code
	 * @param msg
	 * @param success
	 * @return RestResult<T>
	 * @throws 
	 * @create 2018年9月11日 下午4:28:47
	 * @lastUpdate 2018年9月11日 下午4:28:47
	 */
	public static <T> RestResult<T> genResult(T data, int code, String msg,boolean success) {
        RestResult<T> result = RestResult.newInstance();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        result.setSuccess(success);
        log.error(msg);
        return result;
    }
	
	/**
	 * 生成带分页的返回结果
	 * @Title genPageResult
	 * @Description TODO()
	 * @param data
	 * @param code
	 * @param msg
	 * @param success
	 * @param number
	 * @param size
	 * @param totalElements
	 * @param totalPages
	 * @return PageRestResult<T>
	 * @throws 
	 * @create 2018年9月11日 下午4:29:01
	 * @lastUpdate 2018年9月11日 下午4:29:01
	 */
	public static <T> PageRestResult<T> genPageResult(T data, int code, 
			String msg,boolean success,int number, int size, long totalElements, int totalPages){
		PageRestResult<T> result = PageRestResult.newPageInstance();
		result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        result.setSuccess(success);
        result.setNumber(number);
        result.setSize(size);
        result.setTotalElements(totalElements);
        result.setTotalPages(totalPages);
        return result;
	}
	
	/**
	 * 生成带分页的返回结果
	 * @Title genPageResult
	 * @Description TODO()
	 * @param data
	 * @param number
	 * @param size
	 * @param totalElements
	 * @param totalPages
	 * @return PageRestResult<T>
	 * @throws 
	 * @create 2018年9月11日 下午4:29:19
	 * @lastUpdate 2018年9月11日 下午4:29:19
	 */
	public static <T> PageRestResult<T> genPageResult(T data, int number, int size, long totalElements, int totalPages){
		return genPageResult(data,200,null,true, number, size, totalElements, totalPages);
	}
	
	/**
	 * 生成带分页的返回结果
	 * @Title genPageResult
	 * @Description TODO()
	 * @param pageData
	 * @return PageRestResult<List<T>>
	 * @throws 
	 * @create 2018年9月11日 下午4:29:30
	 * @lastUpdate 2018年9月11日 下午4:29:30
	 */
	public static <T> PageRestResult<List<T>> genPageResult(Page<T> pageData){
		return genPageResult(pageData.getContent(),200,null,true,pageData.getNumber(),pageData.getSize(),pageData.getTotalElements(),pageData.getTotalPages());
	}
	
	/**
	 * 返回成功结果
	 * @Title genSuccessResult
	 * @Description TODO()
	 * @param data
	 * @return RestResult<T>
	 * @throws 
	 * @create 2018年9月11日 下午4:30:00
	 * @lastUpdate 2018年9月11日 下午4:30:00
	 */
	public static <T> RestResult<T> genSuccessResult(T data) {

        return genResult(data, 200, null, true);
    }
	
	/**
	 * 返回错误结果
	 * @Title genErrorResult
	 * @Description TODO()
	 * @param msg
	 * @return RestResult<T>
	 * @throws 
	 * @create 2018年9月11日 下午4:30:15
	 * @lastUpdate 2018年9月11日 下午4:30:15
	 */
	public static <T> RestResult<T> genErrorResult(String msg) {

        return genResult(null, 500, msg, false);
    }
	
	/**
	 * 返回错误结果
	 * @Title genErrorResult
	 * @Description TODO()
	 * @param code
	 * @param msg
	 * @return RestResult<T>
	 * @throws 
	 * @create 2018年9月11日 下午4:30:36
	 * @lastUpdate 2018年9月11日 下午4:30:36
	 */
	public static <T> RestResult<T> genErrorResult(Integer code, String msg) {

        return genResult(null, code, msg,false);
    }
	
	/**
	 * 返回错误结果
	 * @Title genErrorResult
	 * @Description TODO()
	 * @param error
	 * @return RestResult<T>
	 * @throws 
	 * @create 2018年9月11日 下午4:30:45
	 * @lastUpdate 2018年9月11日 下午4:30:45
	 */
	public static <T> RestResult<T> genErrorResult(ResultEnum error) {

        return genErrorResult(error.getCode(), error.getMsg());
    }
	
	/**
	 * 返回空结果
	 * @Title genSuccessResult
	 * @Description TODO()
	 * @return RestResult
	 * @throws 
	 * @create 2018年9月11日 下午4:30:59
	 * @lastUpdate 2018年9月11日 下午4:30:59
	 */
	@SuppressWarnings("rawtypes")
	public static RestResult genSuccessResult() {
        return genSuccessResult(null);
    }
}
