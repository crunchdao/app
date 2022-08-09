package com.crunchdao.app.service.apikey.plain;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import com.crunchdao.app.service.apikey.dto.ApiKeyDto;

import lombok.Getter;

@Getter
public class PlainApiKeyAuthenticationToken extends AbstractAuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	
	private final ApiKeyDto apiKey;
	
	public PlainApiKeyAuthenticationToken(ApiKeyDto apiKey) {
		super(AuthorityUtils.NO_AUTHORITIES);
		
		this.apiKey = apiKey;
		
		setAuthenticated(true);
	}
	
	@Override
	public Object getCredentials() {
		return apiKey;
	}
	
	@Override
	public Object getPrincipal() {
		return apiKey;
	}
	
}