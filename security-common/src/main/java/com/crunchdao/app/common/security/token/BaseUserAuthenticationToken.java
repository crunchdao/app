package com.crunchdao.app.common.security.token;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;

@Getter
public class BaseUserAuthenticationToken extends AbstractAuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	
	private final UUID userId;
	
	protected BaseUserAuthenticationToken(UUID userId, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		
		this.userId = userId;
		
		super.setAuthenticated(true);
	}
	
	@Override
	public Object getCredentials() {
		return getPrincipal();
	}
	
	@Override
	public Object getPrincipal() {
		return userId;
	}
	
	public static BaseUserAuthenticationToken fromStrings(UUID userId, Collection<String> authorities) {
		return new BaseUserAuthenticationToken(userId, authorities.stream().map(SimpleGrantedAuthority::new).toList());
	}
	
	public static Optional<UUID> extractUserId(Authentication authentication) {
		if (authentication instanceof BaseUserAuthenticationToken token) {
			return Optional.of(token.getUserId());
		}
		
		return Optional.empty();
	}
	
}