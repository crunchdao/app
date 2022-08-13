package com.crunchdao.app.service.register.api.keycloak;

import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class KeycloakUserDto {
	
	private UUID id;
	private String username;
	private String email;
	private String password;
	
}