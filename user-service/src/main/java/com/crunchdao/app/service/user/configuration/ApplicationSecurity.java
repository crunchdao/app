package com.crunchdao.app.service.user.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.crunchdao.app.common.security.configuration.BaseApplicationSecurity;
import com.crunchdao.app.service.user.controller.v1.UserRestControllerV1;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurity extends BaseApplicationSecurity {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		
		http.authorizeHttpRequests().antMatchers(UserRestControllerV1.SELF_ENDPOINT).authenticated();
		http.authorizeHttpRequests().antMatchers(HttpMethod.GET, UserRestControllerV1.ID_ENDPOINT).permitAll();
		http.authorizeHttpRequests().antMatchers("/v*/**").authenticated();
	}
	
}