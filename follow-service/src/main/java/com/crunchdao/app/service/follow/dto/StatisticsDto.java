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
	private final long followers;
	private final long following;
	private final Boolean followed;
	
}