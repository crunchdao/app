package com.crunchdao.app.service.avatar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ImageConversionException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public ImageConversionException(Throwable cause) {
		super(cause);
	}
	
}