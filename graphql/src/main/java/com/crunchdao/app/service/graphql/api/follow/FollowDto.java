package com.crunchdao.app.service.graphql.api.follow;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class FollowDto {
	
	private UUID userId;
	private UUID peerId;
	private LocalDateTime createdAt;
	
}