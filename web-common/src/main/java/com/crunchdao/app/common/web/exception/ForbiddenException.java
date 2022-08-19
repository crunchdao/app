package com.crunchdao.app.common.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "you don't have permission to do this";
	
	public ForbiddenException() {
		super(DEFAULT_MESSAGE);
	}
	
	public ForbiddenException(String message) {
		super(message);
	}
	
}