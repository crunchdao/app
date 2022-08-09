package com.crunchdao.app.common.security.unauthorized;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiErrorResponse;
import io.github.wimdeblauwe.errorhandlingspringbootstarter.mapper.ErrorCodeMapper;
import io.github.wimdeblauwe.errorhandlingspringbootstarter.mapper.ErrorMessageMapper;
import io.github.wimdeblauwe.errorhandlingspringbootstarter.mapper.HttpStatusMapper;

@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
	
	protected final HttpStatusMapper httpStatusMapper;
	protected final ErrorCodeMapper errorCodeMapper;
	protected final ErrorMessageMapper errorMessageMapper;
	protected final ObjectMapper objectMapper;
	
	@Autowired
	public UnauthorizedEntryPoint(HttpStatusMapper httpStatusMapper, ErrorCodeMapper errorCodeMapper, ErrorMessageMapper errorMessageMapper) {
		this(httpStatusMapper, errorCodeMapper, errorMessageMapper, new ObjectMapper());
	}
	
	public UnauthorizedEntryPoint(HttpStatusMapper httpStatusMapper, ErrorCodeMapper errorCodeMapper, ErrorMessageMapper errorMessageMapper, ObjectMapper objectMapper) {
		this.httpStatusMapper = httpStatusMapper;
		this.errorCodeMapper = errorCodeMapper;
		this.errorMessageMapper = errorMessageMapper;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws JsonProcessingException, IOException {
		ApiErrorResponse errorResponse = createResponse(authException);
		
		response.setStatus(errorResponse.getHttpStatus().value());
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
	
	public ApiErrorResponse createResponse(AuthenticationException exception) {
		HttpStatus httpStatus = httpStatusMapper.getHttpStatus(exception, HttpStatus.UNAUTHORIZED);
		String code = errorCodeMapper.getErrorCode(exception);
		String message = errorMessageMapper.getErrorMessage(exception);
		
		return new ApiErrorResponse(httpStatus, code, message);
	}
	
}