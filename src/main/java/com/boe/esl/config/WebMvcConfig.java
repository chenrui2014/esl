package com.boe.esl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 重写请求映射处理器处理版本url映射
 * @ClassName WebMvcConfig
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年9月12日 下午5:38:03
 * @lastUpdate 2018年9月12日 下午5:38:03
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

	@Override
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		CustomRequestMappingHandlerMapping mapping = new CustomRequestMappingHandlerMapping();
		mapping.setOrder(0);
		mapping.setInterceptors(getInterceptors());
		return mapping;
	}

	
}
