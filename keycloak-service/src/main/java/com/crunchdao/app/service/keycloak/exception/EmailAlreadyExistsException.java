package com.crunchdao.app.service.keycloak.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "email already used by another user";
	
	@ResponseErrorProperty
	private final String email;
	
	public EmailAlreadyExistsException(String email) {
		super(DEFAULT_MESSAGE);
		
		this.email = email;
	}
	
}