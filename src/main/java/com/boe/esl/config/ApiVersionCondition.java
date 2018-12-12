package com.boe.esl.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import lombok.extern.slf4j.Slf4j;

/**
 * 适配版本url
 * @ClassName ApiVesrsionCondition
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年9月12日 下午5:37:16
 * @lastUpdate 2018年9月12日 下午5:37:16
 */
@Slf4j
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

	
	/**
	 * 路径中版本的前缀， 这里用 /v[1-9]/的形式
	 */
	private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");
	
	/**
	 * 版本号
	 */
	private int apiVersion;
	
	public ApiVersionCondition(int apiVersion){
        this.apiVersion = apiVersion;
    }
	@Override
	public ApiVersionCondition combine(ApiVersionCondition other) {
		//采用最后定义优先原则，方法上的定义会覆盖类上的定义
		return new ApiVersionCondition(other.getApiVersion());
	}
	
	@Override
	public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
		log.error(request.getPathInfo());
		log.error(request.getContextPath());
		log.error(request.getServletPath());
		Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getServletPath());
        if(m.find()){
            Integer version = Integer.valueOf(m.group(1));
            if(version >= this.apiVersion) // 如果请求的版本号大于配置版本号， 则满足
                return this;
        }
        return null;
	}

	@Override
	public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
		// 优先匹配最新的版本号
        return other.getApiVersion() - this.apiVersion;
	}
	
	public int getApiVersion() {
        return apiVersion;
    }

}
