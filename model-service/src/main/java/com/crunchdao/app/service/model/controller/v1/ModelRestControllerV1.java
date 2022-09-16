package com.crunchdao.app.service.model.controller.v1;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.common.security.token.BaseUserAuthenticationToken;
import com.crunchdao.app.common.web.exception.ForbiddenException;
import com.crunchdao.app.service.model.dto.ModelCreateForm;
import com.crunchdao.app.service.model.dto.ModelDto;
import com.crunchdao.app.service.model.dto.ModelUpdateForm;
import com.crunchdao.app.service.model.exception.ModelNotFoundException;
import com.crunchdao.app.service.model.permission.CanWrite;
import com.crunchdao.app.service.model.service.ModelService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = ModelRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
public class ModelRestControllerV1 {
	
	public static final String ID_VARIABLE = "{id}";
	
	public static final String BASE_ENDPOINT = "/v1/models";
	public static final String PARAM_USER_ID = "userId";
	
	private final ModelService service;
	
	@GetMapping
	public List<ModelDto> list(
		@RequestParam(name = PARAM_USER_ID) UUID userId,
		Pageable pageable,
		Authentication authentication
	) {
		final var includeComments = canIncludeComments(authentication, userId);
		final var models = service.findAllByUserId(userId);
		
		return models.stream()
			.map((model) -> model.toDto(includeComments))
			.toList();
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@CanWrite
	@PutMapping
	public ModelDto create(
		@Validated @RequestBody ModelCreateForm body,
		Authentication authentication
	) {
		if (authentication instanceof BaseUserAuthenticationToken token) {
			return service.create(body, token.getUserId()).toDto(true);
		}
		
		throw new ForbiddenException();
	}
	
	@GetMapping(ID_VARIABLE)
	public ModelDto show(
		@PathVariable UUID id,
		Authentication authentication
	) {
		final var model = service.findById(id).orElseThrow(() -> new ModelNotFoundException(id));
		final var includeComments = canIncludeComments(authentication, model.getUserId());
		
		return model.toDto(includeComments);
	}
	
	@CanWrite
	@PatchMapping(ID_VARIABLE)
	public ModelDto update(
		@PathVariable UUID id,
		@Validated @RequestBody ModelUpdateForm body,
		Authentication authentication
	) {
		var model = service.findById(id).orElseThrow(() -> new ModelNotFoundException(id));
		
		if (authentication instanceof BaseUserAuthenticationToken token) {
			if (!Objects.equals(model.getUserId(), token.getUserId())) {
				throw new ForbiddenException();
			}
			
			return service.update(model, body).toDto(true);
		}
		
		throw new ForbiddenException();
	}
	
	public static boolean canIncludeComments(Authentication authentication, UUID userId) {
		if (authentication instanceof BaseUserAuthenticationToken token) {
			// TODO Should comment only be visible if `model.read` scope is present?
			return Objects.equals(userId, token.getUserId());
		}
		
		return false;
	}
	
}