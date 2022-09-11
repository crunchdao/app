package com.crunchdao.app.service.graphql.controller;

import java.util.UUID;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.graphql.api.follow.FollowDto;
import com.crunchdao.app.service.graphql.api.follow.FollowServiceClient;
import com.crunchdao.app.service.graphql.api.follow.FollowStatisticsDto;
import com.crunchdao.app.service.graphql.api.user.UserDto;
import com.crunchdao.app.service.graphql.api.user.UserServiceClient;
import com.crunchdao.app.service.graphql.dto.PageableDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
class FollowController {
	
	private final FollowServiceClient followServiceClient;
	private final UserServiceClient userServiceClient;
	
	@QueryMapping
	PageResponse<FollowDto> followers(@Argument UUID userId, @Arguments PageableDto pageable) {
		return followServiceClient.list(userId, pageable);
	}
	
	@QueryMapping
	FollowStatisticsDto followersStatistics(@Argument UUID userId) {
		return followServiceClient.showStatistics(userId);
	}
	
	@SchemaMapping(typeName = "Follow")
	UserDto user(FollowDto follow) {
		return userServiceClient.show(follow.getUserId());
	}
	
	@SchemaMapping(typeName = "Follow")
	UserDto peer(FollowDto follow) {
		return userServiceClient.show(follow.getPeerId());
	}
	
}