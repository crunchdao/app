package com.crunchdao.app.service.auth.filter;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.core.Authentication;

import com.crunchdao.app.common.security.filter.AbstractAuthenticationOncePerRequestFilter;
import com.crunchdao.app.common.security.token.UserAuthenticationToken;
import com.crunchdao.app.service.auth.api.auth.ApiKeyDto;
import com.crunchdao.app.service.auth.api.auth.ApiKeyServiceClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends AbstractAuthenticationOncePerRequestFilter {
	
	private final ObjectProvider<ApiKeyServiceClient> apiKeyServiceClient;
	
	@Override
	public Authentication authenticate(HttpServletRequest request) {
		return extractApiKey(request)
			.flatMap(this::callService)
			.orElse(null);
	}
	
	public Optional<UserAuthenticationToken> callService(String plainApiKey) {
		try {
			ApiKeyDto apiKey = apiKeyServiceClient.getObject().getSelf(formatApiKeyAuthorizationHeader(plainApiKey));
			
			return Optional.of(toToken(apiKey));
		} catch (FeignException.Unauthorized | FeignException.Forbidden exception) {
			return Optional.empty();
		}
	}
	
	public static UserAuthenticationToken toToken(ApiKeyDto apiKey) {
		return UserAuthenticationToken.fromStrings(apiKey.getUserId(), apiKey.getScopes());
	}
	
}