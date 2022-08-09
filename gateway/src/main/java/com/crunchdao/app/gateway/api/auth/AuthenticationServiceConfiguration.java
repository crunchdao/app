package com.crunchdao.app.gateway.api.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "service.auth")
public class AuthenticationServiceConfiguration {
	
	private String baseUrl = "http://auth-service";
	private String authenticateEndpoint = "/v1/authenticate";
	
}