package com.crunchdao.app.service.apikey.plain;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.crunchdao.app.common.security.SecurityHttpHeaders;
import com.crunchdao.app.service.apikey.service.ApiKeyService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlainApiKeyAuthenticationFilter extends OncePerRequestFilter {
	
	private final ApiKeyService service;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Authentication authentication = authenticate(request);
		
		if (authentication != null) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response);
	}
	
	public Authentication authenticate(HttpServletRequest request) {
		return extractApiKey(request)
			.flatMap(service::findByPlain)
			.map(PlainApiKeyAuthenticationToken::new)
			.orElse(null);
	}
	
	public static Optional<String> extractApiKey(HttpServletRequest request) {
		return extractApiKey(request.getHeader(HttpHeaders.AUTHORIZATION));
	}
	
	public static Optional<String> extractApiKey(String header) {
		if (header != null) {
			String[] parts = header.trim().split(" ", 2);
			
			if (parts.length == 2 && SecurityHttpHeaders.API_KEY_PREFIX.equalsIgnoreCase(parts[0])) {
				return Optional.of(parts[1]);
			}
		}
		
		return Optional.empty();
	}
	
}