package com.crunchdao.app.service.notification.controller.v1;

import java.util.Objects;
import java.util.UUID;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.common.security.token.BaseUserAuthenticationToken;
import com.crunchdao.app.common.web.exception.ForbiddenException;
import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.notification.entity.Notification;
import com.crunchdao.app.service.notification.exception.NotificationNotFoundException;
import com.crunchdao.app.service.notification.permission.CanRead;
import com.crunchdao.app.service.notification.permission.CanWrite;
import com.crunchdao.app.service.notification.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = NotificationRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "user", description = "Notification related operations.")
public class NotificationRestControllerV1 {
	
	public static final String ID_VARIABLE = "{id}";
	
	public static final String BASE_ENDPOINT = "/v1/notifications";
	public static final String ID_ENDPOINT = BASE_ENDPOINT + "/" + ID_VARIABLE;
	
	private final NotificationService service;
	
	@GetMapping
	@CanRead
	public PageResponse<Notification> list(@ParameterObject Pageable pageable, @RequestParam(required = false) boolean onlyUnread, Authentication authentication) {
		if (authentication instanceof BaseUserAuthenticationToken token) {
			return new PageResponse<>(service.list(token.getUserId(), pageable, onlyUnread));
		}
		
		throw new ForbiddenException();
	}
	
	@DeleteMapping
	@Operation(summary = "Mark all notifications as read.")
	@CanWrite
	public long markAllAsRead(Authentication authentication) {
		if (authentication instanceof BaseUserAuthenticationToken token) {
			return service.markAllAsRead(token.getUserId());
		}
		
		throw new ForbiddenException();
	}
	
	@GetMapping(ID_VARIABLE)
	@Operation(summary = "Show a Notification")
	@CanRead
	public Notification show(@PathVariable UUID id, Authentication authentication) {
		if (authentication instanceof BaseUserAuthenticationToken token) {
			return getNotification(id, token.getUserId());
		}
		
		throw new ForbiddenException();
	}
	
	@DeleteMapping(ID_VARIABLE)
	@Operation(summary = "Mark a notification as read.")
	@CanWrite
	public Notification markAsRead(@PathVariable UUID id, Authentication authentication) {
		if (authentication instanceof BaseUserAuthenticationToken token) {
			Notification notification = getNotification(id, token.getUserId());
			
			return service.markAsRead(notification);
		}
		
		throw new ForbiddenException();
	}
	
	public Notification getNotification(UUID id, UUID userId) {
		Notification notification = service.findById(id).orElseThrow(() -> new NotificationNotFoundException(id));
		
		if (!Objects.equals(userId, notification.getUserId())) {
			throw new ForbiddenException();
		}
		
		return notification;
	}
	
}