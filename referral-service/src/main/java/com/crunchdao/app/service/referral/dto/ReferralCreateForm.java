package com.crunchdao.app.service.referral.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class ReferralCreateForm {
	
	private UUID userId;
	private String code;
	
}