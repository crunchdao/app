package com.crunchdao.app.service.referral.controller.v1;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crunchdao.app.common.security.permission.Authenticated;
import com.crunchdao.app.common.security.token.BaseUserAuthenticationToken;
import com.crunchdao.app.common.web.exception.ForbiddenException;
import com.crunchdao.app.service.referral.dto.ReferralCodeDto;
import com.crunchdao.app.service.referral.entity.ReferralCode;
import com.crunchdao.app.service.referral.exception.ReferralCodeNotEnabledException;
import com.crunchdao.app.service.referral.exception.ReferralCodeNotFoundException;
import com.crunchdao.app.service.referral.service.ReferralCodeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = ReferralCodeRestControllerV1.BASE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "referral", description = "Referral related operations.")
public class ReferralCodeRestControllerV1 {
	
	public static final String ME_PATH = "@me";
	public static final String CODE_VARIABLE = "{code}";
	
	public static final String BASE_ENDPOINT = "/v1/referral-codes";
	public static final String ME_ENDPOINT = BASE_ENDPOINT + "/" + ME_PATH;
	public static final String CODE_ENDPOINT = BASE_ENDPOINT + "/" + CODE_VARIABLE;
	
	private final ReferralCodeService service;
	
	@Authenticated
	@GetMapping(ME_PATH)
	public ReferralCodeDto showMe(
		Authentication authentication
	) {
		if (authentication instanceof BaseUserAuthenticationToken token) {
			return service
				.findByUserId(token.getUserId())
				.map(ReferralCode::toDto)
				.orElseThrow(() -> new ReferralCodeNotFoundException(null));
		}
		
		throw new ForbiddenException();
	}
	
	@GetMapping(CODE_VARIABLE)
	public ReferralCodeDto showCode(
		@PathVariable String code
	) {
		var referralCode = service
			.findByValue(code)
			.orElseThrow(() -> new ReferralCodeNotFoundException(code));
		
		if (!referralCode.isEnabled()) {
			throw new ReferralCodeNotEnabledException(code);
		}
		
		return referralCode.toDto();
	}
	
}