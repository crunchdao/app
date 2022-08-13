package com.crunchdao.app.common.security.handler;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiErrorResponse;
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignExceptionApiExceptionHandler implements ApiExceptionHandler {
	
	private static final String DEFAULT_CODE = "REMOTE";
	private final ObjectMapper objectMapper;
	
	public FeignExceptionApiExceptionHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper.copy();
		this.objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	}
	
	@Override
	public boolean canHandle(Throwable exception) {
		return exception instanceof FeignException;
	}
	
	@Override
	public ApiErrorResponse handle(Throwable exception) {
		return handle((FeignException) exception);
	}
	
	@SuppressWarnings("deprecation")
	public ApiErrorResponse handle(FeignException exception) {
		HttpStatus status = HttpStatus.valueOf(exception.status());
		
		try {
			byte[] body = exception.content();
			if (body != null) {
				ApiErrorResponse response = new ApiErrorResponseWrapped(status, DEFAULT_CODE, exception.getMessage());
				return objectMapper.readerForUpdating(response).readValue(body);
			}
		} catch (Exception ignored) {
			log.warn("Could not update", ignored);
		}
		
		return new ApiErrorResponse(status, DEFAULT_CODE, exception.getMessage());
	}
	
	public static class ApiErrorResponseWrapped extends ApiErrorResponse {
		
		public ApiErrorResponseWrapped(HttpStatus httpStatus, String code, String message) {
			super(httpStatus, code, message);
		}
		
		@JsonAnySetter
		public void addProperty(String property, Object value) {
			getProperties().put(property, value);
		}
		
	}
	
}