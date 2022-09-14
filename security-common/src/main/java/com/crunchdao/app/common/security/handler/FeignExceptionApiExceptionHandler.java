package com.crunchdao.app.common.security.handler;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
		this.objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
            .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
            .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
            .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
	}
	
	@Override
	public boolean canHandle(Throwable exception) {
		if (exception instanceof FeignException feignException) {
			return feignException.status() > 0;
		}
		
		return false;
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
	
	@JsonIgnoreProperties(ignoreUnknown = true)
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