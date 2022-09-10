package com.crunchdao.app.service.follow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class FollowingYourselfException extends IllegalArgumentException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "you can't follow yourself";
	
	public FollowingYourselfException() {
		super(DEFAULT_MESSAGE);
	}
	
}