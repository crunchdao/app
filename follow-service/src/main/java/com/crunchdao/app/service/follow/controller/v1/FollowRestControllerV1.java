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
import org.springframework.web.bind.annotation.PutMapping;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = FollowRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "follow", description = "Follow related operations.")
public class FollowRestControllerV1 {
	
	public static final String ID_VARIABLE = "{userId}";
	public static final String STATISTICS_ID_PATH = ID_VARIABLE + "/statistics";
	public static final String FOLLOWERS_ID_PATH = ID_VARIABLE + "/followers";
	public static final String FOLLOWINGS_ID_PATH = ID_VARIABLE + "/followings";
	
	public static final String BASE_ENDPOINT = "/v1/follow";
	public static final String FOLLOWERS_ENDPOINT = BASE_ENDPOINT + "/" + FOLLOWERS_ID_PATH;
	public static final String FOLLOWINGS_ENDPOINT = BASE_ENDPOINT + "/" + FOLLOWINGS_ID_PATH;
	public static final String STATISTICS_ENDPOINT = BASE_ENDPOINT + "/" + STATISTICS_ID_PATH;
	
	private final FollowService service;
	
	@GetMapping(FOLLOWERS_ID_PATH)
	@Operation(summary = "List people who are following a user.")
	public PageResponse<FollowDto> showFollowers(
		@PathVariable UUID userId,
		Pageable pageable
	) {
		return service.listFollowers(userId, pageable);
	}
	
	@GetMapping(FOLLOWINGS_ID_PATH)
	@Operation(summary = "List people which a user follow.")
	public PageResponse<FollowDto> showFollowings(
		@PathVariable UUID userId,
		Pageable pageable
	) {
		return service.listFollowing(userId, pageable);
	}
	
	@GetMapping(STATISTICS_ID_PATH)
	@Operation(summary = "Get some number about the `userId`.")
	public StatisticsDto statistics(
		@PathVariable UUID userId,
		Authentication authentication
	) {
		UUID authenticatedUserId = BaseUserAuthenticationToken.extractUserId(authentication).orElse(null);
		
		return service.getStatistics(userId, authenticatedUserId);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@CanWrite
	@PutMapping(FOLLOWERS_ID_PATH)
	@Operation(summary = "Follow the `userId`.")
	public FollowDto follow(
		@PathVariable UUID userId,
		Authentication authentication
	) {
		if (authentication instanceof UserAuthenticationToken token) {
			return service.follow(token.getUserId(), userId).toFollowingDto();
		}
		
		throw new OnlyUserException();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@CanWrite
	@DeleteMapping(FOLLOWERS_ID_PATH)
	@Operation(summary = "Unfollow the `userId`.")
	public void unfollow(
		@PathVariable UUID userId,
		Authentication authentication
	) {
		if (authentication instanceof UserAuthenticationToken token) {
			service.unfollow(token.getUserId(), userId);
		} else {
			throw new OnlyUserException();
		}
	}
	
}