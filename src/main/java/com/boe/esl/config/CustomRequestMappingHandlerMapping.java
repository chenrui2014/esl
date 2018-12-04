package com.boe.esl.config;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.boe.esl.annotation.ApiVersion;

/**
 * 自定义处理版本映射
 * @ClassName CustomRequestMappingHandlerMapping
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年9月12日 下午5:36:54
 * @lastUpdate 2018年9月12日 下午5:36:54
 */
public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	@Override
	protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
		ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
		return createCondition(apiVersion);
	}

	@Override
	protected RequestCondition<?> getCustomMethodCondition(Method method) {
		ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        return createCondition(apiVersion);
	}

	private RequestCondition<ApiVersionCondition> createCondition(ApiVersion apiVersion) {
		return apiVersion == null ? null : new ApiVersionCondition(apiVersion.value());
	}
}
