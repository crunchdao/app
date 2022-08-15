package com.crunchdao.app.service.graphql.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.crunchdao.app.common.security.configuration.BaseApplicationSecurity;
import com.crunchdao.app.common.security.interceptor.authentication.SecurityFeignRequestInterceptor;
import com.crunchdao.app.common.security.interceptor.authentication.UserSecurityFeignRequestInterceptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurity extends BaseApplicationSecurity {
	
	@Override
	protected void configureAuthorizeHttpRequests(HttpSecurity http) throws Exception {
		super.configureAuthorizeHttpRequests(http);
	}
	
	@Bean
	SecurityFeignRequestInterceptor securityFeignRequestInterceptor() {
		return new UserSecurityFeignRequestInterceptor();
	}
	
}