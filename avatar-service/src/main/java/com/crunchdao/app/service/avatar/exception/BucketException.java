package com.crunchdao.app.service.avatar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BucketException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public BucketException(Throwable cause) {
		super(cause);
	}
	
}