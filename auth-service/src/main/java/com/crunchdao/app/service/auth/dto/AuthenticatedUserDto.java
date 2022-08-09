package com.crunchdao.app.service.auth.dto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

import com.crunchdao.app.service.auth.api.auth.ApiKeyDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticatedUserDto {
	
	private final UUID userId;
	private final List<String> authorities;
	
	public AuthenticatedUserDto(ApiKeyDto apiKey) {
		this(apiKey.getUserId(), apiKey.getScopes());
	}
	
	public static AuthenticatedUserDto fromGrantedAuthorities(UUID userId, Collection<GrantedAuthority> authorities) {
		return new AuthenticatedUserDto(userId, authorities.stream().map(GrantedAuthority::getAuthority).toList());
	}
	
}