package com.crunchdao.app.service.avatar.configuration;

import java.util.Collection;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.crunchdao.app.common.security.configuration.BaseApplicationSecurity;
import com.crunchdao.app.service.avatar.permission.Scopes;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurity extends BaseApplicationSecurity {
	
	@Override
	protected Collection<? extends GrantedAuthority> getServiceAuthorities() {
		return AuthorityUtils.createAuthorityList(Scopes.WRITE);
	}
	
}