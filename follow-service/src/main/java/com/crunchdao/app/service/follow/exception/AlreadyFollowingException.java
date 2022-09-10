package com.crunchdao.app.service.follow.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyFollowingException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "already following";
	
	private final UUID peerId;
	
	public AlreadyFollowingException(UUID peerId, Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
		
		this.peerId = peerId;
	}
	
}