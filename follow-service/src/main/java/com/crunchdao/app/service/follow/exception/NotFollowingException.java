package com.crunchdao.app.service.follow.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFollowingException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "not following";
	
	private final UUID peerId;
	
	public NotFollowingException(UUID peerId) {
		super(DEFAULT_MESSAGE);
		
		this.peerId = peerId;
	}
	
}