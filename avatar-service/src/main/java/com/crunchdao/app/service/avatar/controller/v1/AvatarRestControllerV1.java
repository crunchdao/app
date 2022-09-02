package com.crunchdao.app.service.avatar.controller.v1;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.crunchdao.app.common.security.permission.Authenticated;
import com.crunchdao.app.common.security.token.BaseUserAuthenticationToken;
import com.crunchdao.app.common.web.exception.ForbiddenException;
import com.crunchdao.app.service.avatar.permission.CanWrite;
import com.crunchdao.app.service.avatar.service.AvatarService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = AvatarRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "avatar", description = "Avatar related operations.")
public class AvatarRestControllerV1 {
	
	public static final String USER_ID_VARIABLE = "{userId}";
	public static final String SELF_PATH = "@self";
	
	public static final String BASE_ENDPOINT = "/v1/avatar";
	public static final String USER_ID_ENDPOINT = BASE_ENDPOINT + "/" + USER_ID_VARIABLE;
	public static final String SELF_ENDPOINT = BASE_ENDPOINT + "/" + SELF_PATH;
	
	private final AvatarService avatarService;
	
	@CanWrite
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	@SneakyThrows
	public void upload(@RequestPart("picture") MultipartFile file, Authentication authentication) {
		if (authentication instanceof BaseUserAuthenticationToken token) {
			avatarService.upload(file.getInputStream(), token.getUserId());
		} else {
			throw new ForbiddenException();
		}
	}
	
	@GetMapping(USER_ID_VARIABLE)
	public Object show(@PathVariable UUID userId) {
		RedirectView redirectView = new RedirectView(avatarService.toUrl(userId));
		redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
		
		return redirectView;
	}
	
	@Authenticated
	@GetMapping(SELF_PATH)
	public Object showSelf(Authentication authentication) {
		if (authentication instanceof BaseUserAuthenticationToken token) {
			return show(token.getUserId());
		}
		
		throw new ForbiddenException();
	}
	
}