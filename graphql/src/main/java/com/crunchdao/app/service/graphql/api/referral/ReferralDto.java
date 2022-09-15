package com.crunchdao.app.service.graphql.api.referral;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class ReferralDto {
	
	private UUID userId;
	private String code;
	private boolean validated;
	private LocalDateTime validatedAt;
	private LocalDateTime createdAt;
	
}