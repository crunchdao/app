package com.crunchdao.app.service.graphql.api.follow;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.graphql.dto.PageableDto;

@FeignClient("follow-service")
public interface FollowServiceClient {
	
	@GetMapping("/v1/follow/{userId}/followers")
	PageResponse<FollowDto> listFollowers(@PathVariable UUID userId, @SpringQueryMap PageableDto pageable);
	
	@GetMapping("/v1/follow/{userId}/followings")
	PageResponse<FollowDto> listFollowings(@PathVariable UUID userId, @SpringQueryMap PageableDto pageable);
	
	@GetMapping("/v1/follow/{userId}/statistics")
	FollowStatisticsDto showStatistics(@PathVariable UUID userId);
	
}