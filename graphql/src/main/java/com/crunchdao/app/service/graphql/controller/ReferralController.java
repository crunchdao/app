package com.crunchdao.app.service.graphql.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.graphql.api.referral.ReferralCodeDto;
import com.crunchdao.app.service.graphql.api.referral.ReferralDto;
import com.crunchdao.app.service.graphql.api.referral.ReferralServiceClient;
import com.crunchdao.app.service.graphql.api.user.UserDto;
import com.crunchdao.app.service.graphql.api.user.UserServiceClient;
import com.crunchdao.app.service.graphql.dto.PageableDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
class ReferralController {
	
	private final ReferralServiceClient referralServiceClient;
	private final UserServiceClient userServiceClient;
	
	@QueryMapping
	PageResponse<ReferralDto> referrals(@Arguments PageableDto pageable) {
		return referralServiceClient.listReferrals(pageable);
	}
	
	@QueryMapping
	ReferralCodeDto referralCode(@Argument String code) {
		return referralServiceClient.showReferralCode(code);
	}
	
	@QueryMapping
	ReferralCodeDto myReferralCode() {
		return referralServiceClient.showMyReferralCode();
	}
	
	@SchemaMapping(typeName = "Referral")
	UserDto user(ReferralDto referral) {
		return userServiceClient.show(referral.getUserId());
	}
	
	@SchemaMapping(typeName = "ReferralCode")
	UserDto user(ReferralCodeDto referralCode) {
		return userServiceClient.show(referralCode.getUserId());
	}
	
}