package com.crunchdao.app.common.security.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.crunchdao.app.common.security.interceptor.authentication.SecurityFeignRequestInterceptor;
import com.crunchdao.app.common.security.interceptor.authentication.ServiceSecurityFeignRequestInterceptor;

import feign.Feign;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Feign.class)
public class FeignSecurityAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	SecurityFeignRequestInterceptor securityFeignRequestInterceptor(ApplicationContext applicationContext) {
		return new ServiceSecurityFeignRequestInterceptor(applicationContext);
	}
	
}