package com.crunchdao.app.service.keycloak.dto;

import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDto {
	
	private UUID id;
	private String username;
	private String email;
	
}