package com.crunchdao.app.service.keycloak.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class KeycloakException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String UNAUTHORIZED_MESSAGE = "client is unauthorized";
	public static final String FORBIDDEN_MESSAGE = "client is forbidden";
	
	@ResponseErrorProperty
	private final HttpStatus status;
	
	@ResponseErrorProperty
	private final String body;
	
	public KeycloakException(String message) {
		super(message);
		
		this.status = null;
		this.body = null;
	}
	
	public KeycloakException(HttpStatus status, String body) {
		this.status = status;
		this.body = body;
	}
	
	public static KeycloakException unauthorized() {
		return new KeycloakException(UNAUTHORIZED_MESSAGE);
	}
	
	public static KeycloakException forbidden() {
		return new KeycloakException(FORBIDDEN_MESSAGE);
	}
	
}