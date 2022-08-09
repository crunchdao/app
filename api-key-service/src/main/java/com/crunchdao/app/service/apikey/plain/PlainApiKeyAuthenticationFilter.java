package com.crunchdao.app.service.apikey.plain;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import com.crunchdao.app.common.security.filter.AbstractAuthenticationOncePerRequestFilter;
import com.crunchdao.app.service.apikey.service.ApiKeyService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlainApiKeyAuthenticationFilter extends AbstractAuthenticationOncePerRequestFilter {
	
	private final ApiKeyService service;
	
	@Override
	public Authentication authenticate(HttpServletRequest request) {
		return extractApiKey(request)
			.flatMap(service::findByPlain)
			.map(PlainApiKeyAuthenticationToken::new)
			.orElse(null);
	}
	
}