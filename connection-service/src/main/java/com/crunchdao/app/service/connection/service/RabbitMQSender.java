package com.crunchdao.app.service.connection.service;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.crunchdao.app.service.connection.configuration.MessagingConfigurationProperties;
import com.crunchdao.app.service.connection.dto.ConnectionDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RabbitMQSender {
	
	private final RabbitTemplate rabbitTemplate;
	private final MessagingConfigurationProperties properties;
	
	public void sendCreated(ConnectionDto connection) {
		String routingKey = properties.getRoutingKey().getConnection().getEvent().getCreated();
		
		rabbitTemplate.convertAndSend(getConnectionExchange(), routingKey, connection);
	}
	
	public void sendDeleted(UUID userId, String type) {
		sendDeleted(new DeletedConnection(userId, type));
	}
	
	public void sendDeleted(DeletedConnection connection) {
		String routingKey = properties.getRoutingKey().getConnection().getEvent().getDeleted();
		
		rabbitTemplate.convertAndSend(getConnectionExchange(), routingKey, connection);
	}
	
	public String getConnectionExchange() {
		return properties.getExchange().getUser();
	}
	
	@AllArgsConstructor
	@Data
	public static class DeletedConnection {
		
		private final UUID userId;
		private final String type;
		
	}
	
}