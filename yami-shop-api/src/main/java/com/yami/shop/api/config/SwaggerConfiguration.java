/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger文档，只有在测试环境才会使用
 * @author LGH
 */
//@Profile("dev")
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	 @Bean
	 public Docket baseRestApi() {
	     return new Docket(DocumentationType.SWAGGER_2)
	     .apiInfo(apiInfo())
		 .groupName("基础版")
	     .select()
	     .apis(RequestHandlerSelectors.basePackage("com.yami.shop.api"))
	     .paths(PathSelectors.any())
	     .build();
	 }

	 @Bean
	 public ApiInfo apiInfo() {
	     return new ApiInfoBuilder()
	     .title("亚米商城接口文档")
	     .description("亚米商城接口文档Swagger版")
	     .termsOfServiceUrl("http://www.gz-yami.com/")
	     .contact(new Contact("广州亚米信息科技有限公司","https://www.gz-yami.com/", ""))
	     .version("1.0")
	     .build();
	 }
}