package com.crunchdao.app.service.referral.consumer;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.crunchdao.app.service.referral.entity.ReferralCode;
import com.crunchdao.app.service.referral.service.ReferralCodeService;
import com.crunchdao.app.service.referral.service.ReferralService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RegistrationConsumer {
	
	private final ReferralService referralService;
	private final ReferralCodeService referralCodeService;
	
	@RabbitListener(queues = "${app.messaging.queue.registration.event.registered}")
	public void onRegister(RegisteredUserDto registeredUser) {
		if (StringUtils.hasText(registeredUser.getReferralCode())) {
			referralCodeService.findByValue(registeredUser.getReferralCode())
				.filter(ReferralCode::isEnabled)
				.ifPresent((code) -> referralService.refer(registeredUser.getId(), code.getUserId(), code.getValue()));
		}
		
		referralCodeService.create(registeredUser.getId());
	}
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class RegisteredUserDto {
		
		private UUID id;
		private String referralCode;
		
	}
	
}