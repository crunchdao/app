package com.crunchdao.app.service.avatar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.j256.simplemagic.ContentType;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty;
import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RejectedFileContentException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_MESSAGE = "file content is not acceptable";
	
	@ResponseErrorProperty
	private final ContentType contentType;
	
	public RejectedFileContentException(ContentType contentType) {
		super(DEFAULT_MESSAGE);
		
		this.contentType = contentType;
	}
	
}