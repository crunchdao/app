package com.crunchdao.app.service.referral.dto;

import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReferralCodeDto {
	
	private UUID userId;
	private String value;
	
}