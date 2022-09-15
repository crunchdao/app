package com.crunchdao.app.service.graphql.api.referral;

import java.util.UUID;

import lombok.Data;

@Data
public class ReferralCodeDto {
	
	private UUID userId;
	private String value;
	
}