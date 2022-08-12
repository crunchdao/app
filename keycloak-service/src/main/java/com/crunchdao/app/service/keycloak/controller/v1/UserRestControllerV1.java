package com.crunchdao.app.service.keycloak.controller.v1;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.common.security.permission.OnlyService;
import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.keycloak.dto.KeycloakError;
import com.crunchdao.app.service.keycloak.dto.UserDto;
import com.crunchdao.app.service.keycloak.dto.UserWithPasswordDto;
import com.crunchdao.app.service.keycloak.exception.EmailAlreadyExistsException;
import com.crunchdao.app.service.keycloak.exception.KeycloakException;
import com.crunchdao.app.service.keycloak.exception.UserNotFoundException;
import com.crunchdao.app.service.keycloak.exception.UsernameAlreadyExistsException;
import com.crunchdao.app.service.keycloak.util.ResourceUtils;

import lombok.SneakyThrows;

@Validated
@RestController
@RequestMapping(path = UserRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@OnlyService
public class UserRestControllerV1 {
	
	public static final String ID_VARIABLE = "{id}";
	
	public static final String BASE_ENDPOINT = "/v1/users";
	public static final String ID_ENDPOINT = BASE_ENDPOINT + "/" + ID_VARIABLE;
	
	private final UsersResource usersResource;
	
	public UserRestControllerV1(RealmResource realmResource) {
		this.usersResource = realmResource.users();
	}
	
	@GetMapping
	public PageResponse<UserDto> list(Pageable pageable) {
		return run(() -> {
			long total = usersResource.count();
			List<UserRepresentation> content = usersResource.list((int) pageable.getOffset(), pageable.getPageSize());
			
			Page<UserRepresentation> page = new PageImpl<>(content, pageable, total);
			
			return new PageResponse<>(page, UserRestControllerV1::toDto);
		});
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping
	public UserDto create(@RequestBody UserWithPasswordDto body) {
		return run(() -> {
			return doCreate(body);
		});
	}
	
	private UserDto doCreate(@Validated UserWithPasswordDto body) {
		UserRepresentation userRepresentation = new UserRepresentation();
		
		userRepresentation.setEnabled(true);
		userRepresentation.setUsername(body.getUsername());
		userRepresentation.setEmail(body.getEmail());
		
		CredentialRepresentation credentials = new CredentialRepresentation();
		credentials.setTemporary(false);
		credentials.setType(CredentialRepresentation.PASSWORD);
		credentials.setValue(body.getPassword());
		userRepresentation.setCredentials(Collections.singletonList(credentials));
		
		Response response = usersResource.create(userRepresentation);
		throwStatus(response, body);
		
		UUID id = ResourceUtils.extractId(response, true);
		return show(id);
	}
	
	@GetMapping(ID_VARIABLE)
	public UserDto show(@PathVariable UUID id) {
		return run(() -> {
			try {
				return toDto(usersResource.get(id.toString()));
			} catch (NotFoundException exception) {
				throw new UserNotFoundException(id);
			}
		});
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(ID_VARIABLE)
	public void delete(@PathVariable UUID id) {
		run(() -> {
			show(id);
			
			usersResource.delete(id.toString());
			
			return null;
		});
	}
	
	@SneakyThrows
	public static <T> T run(Supplier<T> runner) {
		try {
			return runner.get();
			
		} catch (Throwable exception) {
			if (exception instanceof ProcessingException) {
				exception = exception.getCause();
			}
			
			if (exception instanceof NotAuthorizedException) {
				throw KeycloakException.unauthorized();
			}
			
			if (exception instanceof ForbiddenException) {
				throw KeycloakException.forbidden();
			}
			
			if (exception instanceof ClientErrorException error) {
				Response response = error.getResponse();
				
				HttpStatus status = HttpStatus.valueOf(response.getStatus());
				String body = response.readEntity(String.class);
				
				throw new KeycloakException(status, body);
			}
			
			throw exception;
		}
	}
	
	public static UserDto toDto(UserResource userResource) {
		return toDto(userResource.toRepresentation());
	}
	
	public static UserDto toDto(UserRepresentation userRepresentation) {
		return new UserDto()
			.setId(UUID.fromString(userRepresentation.getId()))
			.setUsername(userRepresentation.getUsername())
			.setEmail(userRepresentation.getEmail());
	}
	
	@SneakyThrows
	public static void throwStatus(Response response, UserWithPasswordDto body) {
		HttpStatus status = HttpStatus.valueOf(response.getStatus());
		if (!status.isError()) {
			return;
		}
		
		KeycloakError error = response.readEntity(KeycloakError.class);
		
		if (HttpStatus.CONFLICT.equals(status) && error.is(KeycloakError.Messages.SAME_USERNAME)) {
			throw new UsernameAlreadyExistsException(body.getUsername());
		}
		
		if (HttpStatus.CONFLICT.equals(status) && error.is(KeycloakError.Messages.SAME_EMAIL)) {
			throw new EmailAlreadyExistsException(body.getEmail());
		}
		
		if (error.getMessage() == null) {
			throw new KeycloakException(status, String.valueOf(error.getExtra()));
		}
		
		throw new KeycloakException(error.getMessage());
	}
	
}