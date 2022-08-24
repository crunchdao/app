package com.crunchdao.app.service.registration.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.crunchdao.app.common.security.configuration.BaseApplicationSecurity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurity extends BaseApplicationSecurity {
	
	@Override
	protected void configureAuthorizeHttpRequests(HttpSecurity http) throws Exception {
		super.configureAuthorizeHttpRequests(http);
		
		http.authorizeHttpRequests().antMatchers("/v*/**").permitAll();
	}
	
}