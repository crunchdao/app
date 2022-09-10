package com.crunchdao.app.service.follow.controller.v1;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.common.security.token.BaseUserAuthenticationToken;
import com.crunchdao.app.common.security.token.UserAuthenticationToken;
import com.crunchdao.app.common.web.exception.OnlyUserException;
import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.follow.dto.FollowDto;
import com.crunchdao.app.service.follow.dto.StatisticsDto;
import com.crunchdao.app.service.follow.permission.CanWrite;
import com.crunchdao.app.service.follow.service.FollowService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = FollowerRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "follow", description = "Follow related operations.")
public class FollowerRestControllerV1 {
	
	public static final String ID_VARIABLE = "{peerId}";
	public static final String STATISTICS_ID_PATH = ID_VARIABLE + "/statistics";
	
	public static final String BASE_ENDPOINT = "/v1/followers";
	public static final String ID_ENDPOINT = BASE_ENDPOINT + "/" + ID_VARIABLE;
	public static final String STATISTICS_ENDPOINT = BASE_ENDPOINT + "/" + STATISTICS_ID_PATH;
	
	private final FollowService service;
	
	@GetMapping(ID_VARIABLE)
	public PageResponse<FollowDto> list(
		@PathVariable UUID peerId,
		Pageable pageable
	) {
		return service.list(peerId, pageable);
	}
	
	@GetMapping(STATISTICS_ID_PATH)
	public StatisticsDto statistics(
		@PathVariable UUID peerId,
		Authentication authentication
	) {
		UUID authenticatedUserId = BaseUserAuthenticationToken.extractUserId(authentication).orElse(null);
		
		return service.getStatistics(peerId, authenticatedUserId);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@CanWrite
	@PostMapping(ID_VARIABLE)
	public FollowDto follow(
		@PathVariable UUID peerId,
		Authentication authentication
	) {
		if (authentication instanceof UserAuthenticationToken token) {
			return service.follow(token.getUserId(), peerId);
		}
		
		throw new OnlyUserException();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@CanWrite
	@DeleteMapping(ID_VARIABLE)
	public void unfollow(
		@PathVariable UUID peerId,
		Authentication authentication
	) {
		if (authentication instanceof UserAuthenticationToken token) {
			service.unfollow(token.getUserId(), peerId);
		} else {
			throw new OnlyUserException();
		}
	}
	
}