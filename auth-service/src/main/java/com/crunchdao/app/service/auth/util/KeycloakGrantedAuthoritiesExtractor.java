package com.crunchdao.app.service.auth.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KeycloakGrantedAuthoritiesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {
	
	private final String rolePrefix = "ROLE_";
	
	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		List<String> roles = extractRoles(jwt);
		
		return toGrantedAuthorities(roles);
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> extractRoles(Jwt jwt) {
		Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().getOrDefault("realm_access", Collections.emptyMap());
		
		return (List<String>) realmAccess.getOrDefault("roles", Collections.emptyList());
	}
	
	public List<GrantedAuthority> toGrantedAuthorities(List<String> roles) {
		return roles.stream()
			.map(rolePrefix::concat)
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
	}
	
}