package com.crunchdao.app.service.auth.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;

import com.crunchdao.app.common.security.configuration.BaseApplicationSecurity;
import com.crunchdao.app.service.auth.api.auth.ApiKeyServiceClient;
import com.crunchdao.app.service.auth.filter.ApiKeyAuthenticationFilter;
import com.crunchdao.app.service.auth.util.KeycloakGrantedAuthoritiesExtractor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends BaseApplicationSecurity {
	
	private final ObjectProvider<ApiKeyServiceClient> apiKeyServiceClient;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		
		http.oauth2ResourceServer()
			.authenticationEntryPoint(unauthorizedEntryPoint)
			.jwt().jwtAuthenticationConverter(createJwtAuthenticationConverter());
	}
	
	@Override
	protected void configureFilters(HttpSecurity http) {
		http.addFilterBefore(new ApiKeyAuthenticationFilter(apiKeyServiceClient), BearerTokenAuthenticationFilter.class);
	}
	
	public static JwtAuthenticationConverter createJwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakGrantedAuthoritiesExtractor());
		
		return jwtAuthenticationConverter;
	}
	
}