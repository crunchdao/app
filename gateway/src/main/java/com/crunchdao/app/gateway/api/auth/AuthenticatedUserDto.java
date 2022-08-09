package com.crunchdao.app.gateway.api.auth;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class AuthenticatedUserDto {
	
	private UUID userId;
	private List<String> authorities;
	
}