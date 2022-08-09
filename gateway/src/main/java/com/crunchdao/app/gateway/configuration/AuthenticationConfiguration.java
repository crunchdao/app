package com.crunchdao.app.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.crunchdao.app.gateway.api.auth.AuthenticationServiceConfiguration;
import com.crunchdao.app.gateway.filter.AuthenticationWebFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class AuthenticationConfiguration {
	
	@Bean
	AuthenticationWebFilter authenticationWebFilter(WebClient.Builder webClientBuilder, AuthenticationServiceConfiguration serviceConfiguration) {
		WebClient webClient = webClientBuilder
			.baseUrl(serviceConfiguration.getBaseUrl())
			.build();
		
		return new AuthenticationWebFilter(serviceConfiguration.getAuthenticateEndpoint(), webClient);
	}
	
}