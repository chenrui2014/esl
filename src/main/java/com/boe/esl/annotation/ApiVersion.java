package com.boe.esl.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.Mapping;

/**
 * 版本注解
 * @ClassName ApiVersion
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年9月12日 下午5:37:39
 * @lastUpdate 2018年9月12日 下午5:37:39
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {

	/**
	 * 版本号
	 * @Title value
	 * @Description TODO()
	 * @return int
	 * @throws 
	 * @create 2018年9月12日 上午10:52:12
	 * @lastUpdate 2018年9月12日 上午10:52:12
	 */
	int value();
}
