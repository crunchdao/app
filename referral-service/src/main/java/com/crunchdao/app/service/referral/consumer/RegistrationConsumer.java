package com.crunchdao.app.service.referral.consumer;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.crunchdao.app.service.referral.entity.ReferralCode;
import com.crunchdao.app.service.referral.service.ReferralCodeService;
import com.crunchdao.app.service.referral.service.ReferralService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class RegistrationConsumer {
	
	private final ReferralService referralService;
	private final ReferralCodeService referralCodeService;
	
	@RabbitListener(queues = "${app.messaging.queue.registration.event.registered}")
	public void onRegister(RegisteredUserDto registeredUser) {
		log.info("RegistrationConsumer.onRegister({})", registeredUser);
		
		if (StringUtils.hasText(registeredUser.getReferralCode())) {
			referralCodeService.findByValue(registeredUser.getReferralCode())
				.filter(ReferralCode::isEnabled)
				.ifPresent((code) -> referralService.refer(registeredUser.getId(), code.getUserId(), code.getValue()));
		}
		
		referralCodeService.create(registeredUser.getId());
	}

	@RabbitListener(queues = "${app.messaging.queue.registration.event.resigned}")
	@Transactional
	public void onResign(ResignedUserDto resignedUser) {
		log.info("RegistrationConsumer.onResign({})", resignedUser);
		
		referralService.onUserDeleted(resignedUser.getId());
		referralCodeService.onUserDeleted(resignedUser.getId());
	}
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class RegisteredUserDto {
		
		private UUID id;
		private String referralCode;
		
	}
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ResignedUserDto {
		
		private UUID id;
		
	}
	
}