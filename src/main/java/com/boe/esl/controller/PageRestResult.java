package com.boe.esl.controller;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 带分页的返回结果
 * @ClassName PageRestResult
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年9月11日 下午4:28:07
 * @lastUpdate 2018年9月11日 下午4:28:07
 * @param <T>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageRestResult<T> extends RestResult<T> {

	/**
	 * @field serialVersionUID:TODO()
	 */
	private static final long serialVersionUID = 1L;
	
	private int number;
	private int size;
	private long totalElements;
	private int totalPages;
	
	private PageRestResult() {}
	
	public static <T> PageRestResult<T> newPageInstance() {
        return new PageRestResult<>();
    }

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

}
