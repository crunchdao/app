package com.crunchdao.app.service.connection.controller.v1;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.common.security.token.BaseUserAuthenticationToken;
import com.crunchdao.app.common.security.token.UserAuthenticationToken;
import com.crunchdao.app.common.web.exception.ForbiddenException;
import com.crunchdao.app.common.web.exception.OnlyUserException;
import com.crunchdao.app.service.connection.dto.ConnectionDto;
import com.crunchdao.app.service.connection.dto.ConnectionUpdateForm;
import com.crunchdao.app.service.connection.dto.HandlerDescriptionDto;
import com.crunchdao.app.service.connection.dto.RedirectDto;
import com.crunchdao.app.service.connection.handler.ConnectionIdentity;
import com.crunchdao.app.service.connection.permission.CanWrite;
import com.crunchdao.app.service.connection.service.ConnectionHandlerService;
import com.crunchdao.app.service.connection.service.ConnectionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping(path = ConnectionRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "connection", description = "Connection related operations.")
public class ConnectionRestControllerV1 {
	
	public static final String PATH_HANDLERS = "handlers";
	public static final String TYPE_VARIABLE = "{type}";
	public static final String PATH_CALLBACK = "{type}/callback";
	
	public static final String BASE_ENDPOINT = "/v1/connections";
	public static final String TYPE_ENDPOINT = BASE_ENDPOINT + "/" + TYPE_VARIABLE;
	public static final String HANDLERS_ENDPOINT = BASE_ENDPOINT + "/" + PATH_HANDLERS;
	public static final String CALLBACK_ENDPOINT = BASE_ENDPOINT + "/" + PATH_CALLBACK;
	
	public static final String PARAMETER_USER = "user";
	
	private final ConnectionService connectionService;
	private final ConnectionHandlerService connectionHandlerService;
	
	@GetMapping
	@Operation(summary = "List connections.")
	public List<ConnectionDto> list(@RequestParam(name = PARAMETER_USER, required = false) UUID userId, Authentication authentication) {
		if (userId != null) {
			return connectionService.listPublicForUserId(userId);
		}
		
		if (authentication == null) {
			throw new ForbiddenException("`user` is not specified");
		}
		
		if (authentication instanceof BaseUserAuthenticationToken token) {
			return connectionService.listForUserId(token.getUserId());
		}
		
		throw new ForbiddenException();
	}
	
	@CanWrite
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Remove a connection.")
	public void disconnectAll(Authentication authentication) {
		if (authentication instanceof UserAuthenticationToken token) {
			connectionService.disconnectAll(token.getUserId());
		} else {
			throw new OnlyUserException();
		}
	}
	
	@GetMapping(PATH_HANDLERS)
	@Operation(summary = "List available handlers.")
	public List<HandlerDescriptionDto> listHandlers() {
		return connectionHandlerService.getHandlerTypes();
	}

	@CanWrite
	@PatchMapping(TYPE_VARIABLE)
	@Operation(summary = "Update a connection.")
	public ConnectionDto update(@PathVariable String type, @RequestBody @Validated ConnectionUpdateForm body, Authentication authentication) {
		if (authentication instanceof UserAuthenticationToken token) {
			return connectionService.update(token.getUserId(), type, body);
		}
		
		throw new OnlyUserException();
	}
	
	@CanWrite
	@PostMapping(TYPE_VARIABLE)
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Start a connection.")
	public RedirectDto connect(@PathVariable String type, Authentication authentication) {
		if (authentication instanceof UserAuthenticationToken token) {
			String url = connectionHandlerService.generateUrl(token.getUserId(), type);
			
			return new RedirectDto(url);
		}
		
		throw new OnlyUserException();
	}

	@CanWrite
	@PostMapping(PATH_CALLBACK)
	@Operation(summary = "OAuth callback.")
	public ConnectionDto connectCallback(@PathVariable String type, @RequestParam String code, Authentication authentication) throws Exception {
		if (authentication instanceof UserAuthenticationToken token) {
			ConnectionIdentity identity = connectionHandlerService.fetchIdentity(token.getUserId(), type, code);
			
			return connectionService.connect(token.getUserId(), type, identity);
		}
		
		throw new OnlyUserException();
	}

	@CanWrite
	@DeleteMapping(TYPE_VARIABLE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "Remove a connection.")
	public void disconnect(@PathVariable String type, Authentication authentication) {
		if (authentication instanceof UserAuthenticationToken token) {
			connectionService.disconnect(token.getUserId(), type);
		} else {
			throw new OnlyUserException();
		}
	}
	
}