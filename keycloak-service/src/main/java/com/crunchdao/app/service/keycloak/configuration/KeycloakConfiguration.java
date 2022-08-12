package com.crunchdao.app.service.keycloak.configuration;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfiguration {
	
	@Bean
	Keycloak keycloak(KeycloakConfigurationProperties properties) {
		return KeycloakBuilder.builder()
			.grantType(OAuth2Constants.CLIENT_CREDENTIALS)
			.serverUrl(properties.getServerUrl())
			.realm(properties.getRealm())
			.clientId(properties.getClientId())
			.clientSecret(properties.getClientSecret())
			.build();
	}
	
	@Bean
	RealmResource realmResource(Keycloak keycloak, KeycloakConfigurationProperties properties) {
		return keycloak.realm(properties.getRealm());
	}
	
}