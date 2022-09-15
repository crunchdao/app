package com.crunchdao.app.service.graphql.api.referral;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.graphql.dto.PageableDto;

@FeignClient("referral-service")
public interface ReferralServiceClient {
	
	@GetMapping("/v1/referrals")
	PageResponse<ReferralDto> listReferrals(@SpringQueryMap PageableDto pageable);
	
	@GetMapping("/v1/referral-codes/{code}")
	ReferralCodeDto showReferralCode(@PathVariable String code);
	
	@GetMapping("/v1/referral-codes/@me")
	ReferralCodeDto showMyReferralCode();
	
}