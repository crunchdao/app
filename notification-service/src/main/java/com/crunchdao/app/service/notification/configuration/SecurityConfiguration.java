package com.crunchdao.app.service.notification.configuration;

import java.util.Collection;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.crunchdao.app.common.security.configuration.BaseApplicationSecurity;
import com.crunchdao.app.service.notification.permission.Scopes;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends BaseApplicationSecurity {
	
	@Override
	protected void configureAuthorizeHttpRequests(HttpSecurity http) throws Exception {
		super.configureAuthorizeHttpRequests(http);
		
		http.authorizeHttpRequests().antMatchers("/v*/**").authenticated();
	}
	
	@Override
	protected Collection<? extends GrantedAuthority> getServiceAuthorities() {
		return AuthorityUtils.createAuthorityList(Scopes.READ, Scopes.WRITE);
	}
	
}