package com.crunchdao.app.service.user.controller.v1;

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

import com.crunchdao.app.common.security.permission.Authenticated;
import com.crunchdao.app.common.security.permission.OnlyAdminOrService;
import com.crunchdao.app.common.security.token.BaseUserAuthenticationToken;
import com.crunchdao.app.common.web.exception.ForbiddenException;
import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.user.dto.UserDto;
import com.crunchdao.app.service.user.dto.UserWithIdDto;
import com.crunchdao.app.service.user.exception.UserNotFoundException;
import com.crunchdao.app.service.user.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = UserRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "user", description = "User related operations.")
public class UserRestControllerV1 {
	
	public static final String ID_VARIABLE = "{id}";
	public static final String SELF_ID_VARIABLE = "@self";
	
	public static final String BASE_ENDPOINT = "/v1/users";
	public static final String ID_ENDPOINT = BASE_ENDPOINT + "/" + ID_VARIABLE;
	public static final String SELF_ENDPOINT = BASE_ENDPOINT + "/" + SELF_ID_VARIABLE;
	
	private final UserService service;
	
	@GetMapping
	@OnlyAdminOrService
	@Hidden
	public PageResponse<UserDto> list(@ParameterObject Pageable pageable) {
		return service.list(pageable);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping
	@OnlyAdminOrService
	@Hidden
	public UserDto create(@Validated @RequestBody UserWithIdDto body) {
		return service.create(body);
	}
	
	@GetMapping(ID_VARIABLE)
	@Operation(summary = "Show a User")
	public UserDto show(@PathVariable UUID id) {
		return service.findById(id)
			.map(UserDto::stripSensitive)
			.orElseThrow(() -> new UserNotFoundException(id));
	}
	
	@Authenticated
	@GetMapping(SELF_ID_VARIABLE)
	@Operation(summary = "Show Yourself")
	public UserDto showSelf(Authentication authentication) {
		if (authentication instanceof BaseUserAuthenticationToken token) {
			return service.findById(token.getUserId())
				.orElseThrow(() -> new RuntimeException("authenticated user not found"));
		}
		
		throw new ForbiddenException();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(ID_VARIABLE)
	@OnlyAdminOrService
	@Hidden
	public void delete(@PathVariable UUID id) {
		service.delete(id);
	}
	
}