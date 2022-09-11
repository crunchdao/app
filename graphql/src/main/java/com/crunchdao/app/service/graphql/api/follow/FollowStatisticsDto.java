package com.crunchdao.app.service.graphql.api.follow;

import java.util.UUID;

import lombok.Data;

@Data
public class FollowStatisticsDto {
	
	private UUID userId;
	private long followers;
	private long following;
	private Boolean followed;
	
}