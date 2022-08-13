package com.crunchdao.app.common.security.interceptor;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.crunchdao.app.common.security.SecurityHttpHeaders;
import com.crunchdao.app.common.security.authentication.AuthenticationType;
import com.crunchdao.app.common.security.token.BaseUserAuthenticationToken;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SecurityFeignRequestInterceptor implements RequestInterceptor {
	
	private final ApplicationContext applicationContext;
	
	@Override
	public void apply(RequestTemplate template) {
		template.header(SecurityHttpHeaders.AUTHENTICATION_TYPE, AuthenticationType.SERVICE.name());
		template.header(SecurityHttpHeaders.SERVICE_NAME, applicationContext.getId());
		
		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null) {
			Authentication authentication = context.getAuthentication();
			
			if (authentication instanceof BaseUserAuthenticationToken token) {
				template.header(SecurityHttpHeaders.USER_ID, token.getUserId().toString());
			}
		}
	}
	
}