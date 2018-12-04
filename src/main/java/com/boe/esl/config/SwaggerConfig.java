package com.boe.esl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2 配置
 * @ClassName SwaggerConfig
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年9月14日 下午1:28:44
 * @lastUpdate 2018年9月14日 下午1:28:44
 */
@EnableSwagger2
@Configuration
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");

registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Bean
	public Docket commonDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
		.groupName("ESL API接口文档")
		.apiInfo(apiInfo("ESL标签管理系统接口"))
		.pathMapping("/")
		.select()
		.apis(RequestHandlerSelectors.basePackage("com.boe.esl.controller"))
		.paths(PathSelectors.any())
		.build();
	}
	
	private ApiInfo apiInfo(String des) {
		return new ApiInfoBuilder()
				.title(des)
				.contact(new Contact("chenruixiang", "https://github.com/chenrui2014", "chenrx@boe.com.cn"))
				.version("1.0")
				.description("api 描述")
				.build();
	}
	
}
