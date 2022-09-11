package com.crunchdao.app.service.follow.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(name = "Follow")
public class FollowDto {
	
	private UUID userId;
	private LocalDateTime createdAt;
	
}