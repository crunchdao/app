package com.crunchdao.app.service.auth.controller.v1;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.common.security.token.UserAuthenticationToken;
import com.crunchdao.app.service.auth.dto.AuthenticatedUserDto;

@Validated
@RestController
@RequestMapping(path = "/v1/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticateControllerV1 {
	
	@PreAuthorize("authenticated")
	@GetMapping
	public AuthenticatedUserDto authenticate(Authentication authentication) {
		if (authentication instanceof UserAuthenticationToken token) {
			return AuthenticatedUserDto.fromGrantedAuthorities(token.getUserId(), token.getAuthorities());
		}
		
		if (authentication instanceof JwtAuthenticationToken token) {
			Jwt jwt = token.getToken();
			UUID userId = UUID.fromString(jwt.getSubject());
			
			return AuthenticatedUserDto.fromGrantedAuthorities(userId, token.getAuthorities());
		}
		
		throw new UnsupportedOperationException(String.valueOf(authentication));
	}
	
}