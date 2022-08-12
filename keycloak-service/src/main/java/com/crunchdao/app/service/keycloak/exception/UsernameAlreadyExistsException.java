package com.crunchdao.app.service.keycloak.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "username already used by another user";
	
	@ResponseErrorProperty
	private final String username;
	
	public UsernameAlreadyExistsException(String username) {
		super(DEFAULT_MESSAGE);
		
		this.username = username;
	}
	
}