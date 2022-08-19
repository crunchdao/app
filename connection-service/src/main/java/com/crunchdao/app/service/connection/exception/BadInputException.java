package com.crunchdao.app.service.connection.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadInputException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public BadInputException(String message) {
		super(message);
	}
	
	public static BadInputException invalidCode() {
		return new BadInputException("invalid code");
	}
	
	public static BadInputException couldNotFetch() {
		return new BadInputException("could not fetch personal info");
	}
	
}