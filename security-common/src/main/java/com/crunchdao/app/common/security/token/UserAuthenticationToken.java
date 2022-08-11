package com.crunchdao.app.common.security.token;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;

@Getter
public class UserAuthenticationToken extends BaseUserAuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	
	public UserAuthenticationToken(UUID userId, Collection<? extends GrantedAuthority> authorities) {
		super(Objects.requireNonNull(userId, "userId"), authorities);
		
		super.setAuthenticated(true);
	}
	
	public static UserAuthenticationToken fromStrings(UUID userId, Collection<String> authorities) {
		return new UserAuthenticationToken(userId, authorities.stream().map(SimpleGrantedAuthority::new).toList());
	}
	
}