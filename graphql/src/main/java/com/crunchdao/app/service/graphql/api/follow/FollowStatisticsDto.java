package com.crunchdao.app.service.graphql.api.follow;

import lombok.Data;

@Data
public class FollowStatisticsDto {
	
	private long followers;
	private long followings;
	private Boolean followed;
	
}