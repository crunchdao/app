package com.crunchdao.app.common.security.interceptor.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.crunchdao.app.common.security.SecurityHttpHeaders;
import com.crunchdao.app.common.security.authentication.AuthenticationType;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class SecurityFeignRequestInterceptor implements RequestInterceptor {
	
	@Override
	public void apply(RequestTemplate template) {
		template.header(SecurityHttpHeaders.AUTHENTICATION_TYPE, getAuthenticationType().name());
		
		doApply(template, getAuthentication());
	}
	
	public abstract AuthenticationType getAuthenticationType();
	
	protected abstract void doApply(RequestTemplate template, Authentication authentication);
	
	public static Authentication getAuthentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		
		if (context == null) {
			return null;
		}
		
		return context.getAuthentication();
	}
	
}