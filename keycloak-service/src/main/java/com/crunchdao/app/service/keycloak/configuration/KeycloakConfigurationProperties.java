package com.crunchdao.app.service.keycloak.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@ConfigurationProperties("keycloak")
@Component
public class KeycloakConfigurationProperties {
	
	private String serverUrl;
	private String realm;
	private String clientId;
	private String clientSecret;
	
}