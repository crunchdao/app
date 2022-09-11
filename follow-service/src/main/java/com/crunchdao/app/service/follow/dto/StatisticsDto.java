package com.crunchdao.app.service.follow.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(name = "Statistics")
public class StatisticsDto {
	
	private final UUID userId;

	@Schema(description = "Amount of people that follow the `userId`.")
	private final long followers;
	
	@Schema(description = "Amount of people that the `userId` is currently following.")
	private final long followings;
	
	@Schema(description = "Is the currently authenticated user following the `userId`?")
	private final Boolean followed;
	
}