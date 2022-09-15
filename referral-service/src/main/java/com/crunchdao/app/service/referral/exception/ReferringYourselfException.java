package com.crunchdao.app.service.referral.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReferringYourselfException extends IllegalArgumentException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "you can't refer yourself";
	
	public ReferringYourselfException() {
		super(DEFAULT_MESSAGE);
	}
	
}