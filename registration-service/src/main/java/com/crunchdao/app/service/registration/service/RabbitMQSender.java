package com.crunchdao.app.service.registration.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.crunchdao.app.service.registration.configuration.MessagingConfigurationProperties;
import com.crunchdao.app.service.registration.model.RegisterForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RabbitMQSender {
	
	private final RabbitTemplate rabbitTemplate;
	private final MessagingConfigurationProperties properties;
	
	public void sendRegistered(UUID userId, RegisterForm body) {
		String routingKey = properties.getRoutingKey().getRegistration().getEvent().getRegistered();
		
		rabbitTemplate.convertAndSend(getExchange(), routingKey, new RegisteredUserDto(userId, body.getReferralCode()));
	}
	
	public void sendResigned(UUID userId) {
		String routingKey = properties.getRoutingKey().getRegistration().getEvent().getResigned();
		
		rabbitTemplate.convertAndSend(getExchange(), routingKey, new ResignedUserDto(userId));
	}
	
	public String getExchange() {
		return properties.getExchange().getRegistration();
	}
	
	@AllArgsConstructor
	@Data
	public static class RegisteredUserDto {
		
		private final UUID id;
		private final String referralCode;
		private final LocalDateTime at = LocalDateTime.now();
		
	}
	
	@AllArgsConstructor
	@Data
	public static class ResignedUserDto {
		
		private final UUID id;
		private final LocalDateTime at = LocalDateTime.now();
		
	}
	
}