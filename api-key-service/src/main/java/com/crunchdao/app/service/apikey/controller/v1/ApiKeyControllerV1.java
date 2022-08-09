package com.crunchdao.app.service.apikey.controller.v1;

import java.util.Objects;
import java.util.UUID;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.common.security.token.UserAuthenticationToken;
import com.crunchdao.app.common.web.exception.ForbiddenException;
import com.crunchdao.app.common.web.exception.OnlyUserException;
import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.apikey.dto.ApiKeyDto;
import com.crunchdao.app.service.apikey.exception.ApiKeyNotFoundException;
import com.crunchdao.app.service.apikey.permission.CanRead;
import com.crunchdao.app.service.apikey.permission.CanWrite;
import com.crunchdao.app.service.apikey.service.ApiKeyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = ApiKeyControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "api-key", description = "API-Key related operations.")
public class ApiKeyControllerV1 {
	
	public static final String BASE_ENDPOINT = "/v1/api-keys";
	public static final String ID_VARIABLE = "{id}";
	public static final String ID_ENDPOINT = BASE_ENDPOINT + "/" + ID_VARIABLE;
	
	private final ApiKeyService service;
	
	@GetMapping
	@CanRead
	@Operation(summary = "List user's API-Keys.")
	public PageResponse<ApiKeyDto> list(@ParameterObject Pageable pageable, Authentication authentication) {
		if (authentication instanceof UserAuthenticationToken token) {
			return service.listForUserId(token.getUserId(), pageable);
		}
		
		throw new OnlyUserException();
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping
	@CanWrite
	@Operation(summary = "Create an API-Keys.")
	public ApiKeyDto create(@Validated @RequestBody ApiKeyDto body, Authentication authentication) {
		if (authentication instanceof UserAuthenticationToken token) {
			return service.create(body, token.getUserId());
		}
		
		throw new OnlyUserException();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping
	@CanWrite
	@Operation(summary = "Delete all's user API-Keys.")
	public void deleteAll(Authentication authentication) {
		if (authentication instanceof UserAuthenticationToken token) {
			service.deleteAllByUserId(token.getUserId());
		} else {
			throw new OnlyUserException();
		}
	}
	
	@GetMapping(ID_VARIABLE)
	@CanRead
	@Operation(summary = "Show an API-Key.")
	public ApiKeyDto show(@PathVariable UUID id, Authentication authentication) {
		if (authentication instanceof UserAuthenticationToken token) {
			return getApiKey(id, token);
		}
		
		throw new OnlyUserException();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(ID_VARIABLE)
	@CanWrite
	@Operation(summary = "Delete an API-Key.")
	public void delete(@PathVariable UUID id, Authentication authentication) {
		if (authentication instanceof UserAuthenticationToken token) {
			ApiKeyDto apiKey = getApiKey(id, token);
			
			service.delete(apiKey.getId());
		} else {
			throw new OnlyUserException();
		}
	}
	
	public ApiKeyDto getApiKey(UUID id, UserAuthenticationToken authentication) {
		ApiKeyDto apiKey = service.findById(id).orElseThrow(() -> new ApiKeyNotFoundException(id));
		
		if (!Objects.equals(authentication.getUserId(), apiKey.getUserId())) {
			throw new ForbiddenException();
		}
		
		return apiKey;
	}
	
}