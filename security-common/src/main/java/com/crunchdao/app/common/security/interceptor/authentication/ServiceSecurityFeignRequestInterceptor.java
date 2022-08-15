package com.crunchdao.app.common.security.interceptor.authentication;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;

import com.crunchdao.app.common.security.SecurityHttpHeaders;
import com.crunchdao.app.common.security.authentication.AuthenticationType;
import com.crunchdao.app.common.security.token.BaseUserAuthenticationToken;

import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServiceSecurityFeignRequestInterceptor extends SecurityFeignRequestInterceptor {
	
	private final ApplicationContext applicationContext;
	
	@Override
	public AuthenticationType getAuthenticationType() {
		return AuthenticationType.SERVICE;
	}
	
	@Override
	protected void doApply(RequestTemplate template, Authentication authentication) {
		template.header(SecurityHttpHeaders.SERVICE_NAME, applicationContext.getId());
		
		if (authentication instanceof BaseUserAuthenticationToken token) {
			template.header(SecurityHttpHeaders.USER_ID, token.getUserId().toString());
		}
	}
	
}