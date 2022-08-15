package com.crunchdao.app.common.security.interceptor.authentication;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.crunchdao.app.common.security.SecurityHttpHeaders;
import com.crunchdao.app.common.security.authentication.AuthenticationType;
import com.crunchdao.app.common.security.token.UserAuthenticationToken;

import feign.RequestTemplate;

public class UserSecurityFeignRequestInterceptor extends SecurityFeignRequestInterceptor {
	
	@Override
	public AuthenticationType getAuthenticationType() {
		return AuthenticationType.USER;
	}
	
	@Override
	protected void doApply(RequestTemplate template, Authentication authentication) {
		if (authentication instanceof UserAuthenticationToken token) {
			template.header(SecurityHttpHeaders.USER_ID, token.getUserId().toString());
			template.header(SecurityHttpHeaders.AUTHORITIES, joinAuthorities(token.getAuthorities()));
		}
	}
	
	public static String joinAuthorities(Collection<GrantedAuthority> authorities) {
		return authorities
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));
	}
	
}