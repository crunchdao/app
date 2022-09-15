package com.crunchdao.app.service.referral.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(name = "Referral")
public class ReferralDto {
	
	private UUID userId;
	private String code;
	private boolean validated;
	private LocalDateTime validatedAt;
	private LocalDateTime createdAt;
	
}