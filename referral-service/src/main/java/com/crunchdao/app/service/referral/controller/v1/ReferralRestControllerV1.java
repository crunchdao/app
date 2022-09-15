package com.crunchdao.app.service.referral.controller.v1;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.common.security.token.BaseUserAuthenticationToken;
import com.crunchdao.app.common.web.exception.ForbiddenException;
import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.referral.dto.ReferralDto;
import com.crunchdao.app.service.referral.permission.CanRead;
import com.crunchdao.app.service.referral.service.ReferralService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = ReferralRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "referral", description = "Referral related operations.")
public class ReferralRestControllerV1 {
	
	public static final String BASE_ENDPOINT = "/v1/referrals";
	
	private final ReferralService service;
	
	@CanRead
	@GetMapping
	public PageResponse<ReferralDto> list(
		Pageable pageable,
		Authentication authentication
	) {
		if (authentication instanceof BaseUserAuthenticationToken token) {
			return service.list(token.getUserId(), pageable);
		}
		
		throw new ForbiddenException();
	}
	
}