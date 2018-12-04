package com.boe.esl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.boe.esl.controller.RestResult;
import com.boe.esl.controller.RestResultGenerator;
import com.boe.esl.controller.ResultEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	private <T> RestResult<T> businessExceptionHandler(Exception e) {
		return RestResultGenerator.genErrorResult(ResultEnum.SERVER_ERROR);
	}
	
	@ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private <T> RestResult<T> runtimeExceptionHandler(Exception e) {
        log.error("服务器错误!", e);
        return RestResultGenerator.genErrorResult(ResultEnum.SERVER_ERROR);
    }
}
