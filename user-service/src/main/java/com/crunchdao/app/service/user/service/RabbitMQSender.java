package com.crunchdao.app.service.user.service;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.crunchdao.app.service.user.configuration.MessagingConfigurationProperties;
import com.crunchdao.app.service.user.dto.UserDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RabbitMQSender {
	
	private final RabbitTemplate rabbitTemplate;
	private final MessagingConfigurationProperties properties;
	
	public void sendCreated(UserDto user) {
		String routingKey = properties.getRoutingKey().getUser().getEvent().getCreated();
		
		rabbitTemplate.convertAndSend(getUserExchange(), routingKey, user);
	}
	
	public void sendDeleted(UUID id) {
		String routingKey = properties.getRoutingKey().getUser().getEvent().getDeleted();
		
		rabbitTemplate.convertAndSend(getUserExchange(), routingKey, id);
	}
	
	public String getUserExchange() {
		return properties.getExchange().getUser();
	}
	
}