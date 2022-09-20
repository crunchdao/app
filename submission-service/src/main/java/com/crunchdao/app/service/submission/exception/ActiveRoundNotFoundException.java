package com.crunchdao.app.service.submission.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ActiveRoundNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "active round not found";
	
	public ActiveRoundNotFoundException() {
		super(DEFAULT_MESSAGE);
	}
	
}