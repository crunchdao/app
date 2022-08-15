package com.crunchdao.app.common.security.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.crunchdao.app.common.security.handler.FeignExceptionApiExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Feign;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Feign.class)
public class FeignExceptionAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	FeignExceptionApiExceptionHandler feignExceptionApiExceptionHandler(ObjectMapper objectMapper) {
		return new FeignExceptionApiExceptionHandler(objectMapper);
	}
	
}