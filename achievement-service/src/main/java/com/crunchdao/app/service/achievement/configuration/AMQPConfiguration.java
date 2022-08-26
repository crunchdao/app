package com.crunchdao.app.service.achievement.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration(proxyBeanMethods = false)
public class AMQPConfiguration {
	
	@Bean
	Exchange exchange(MessagingConfigurationProperties properties) {
		return ExchangeBuilder
			.topicExchange(properties.getExchange().getAchievement())
			.build();
	}
	
	@Configuration(proxyBeanMethods = false)
	public static class UserAMQPConfiguration {
		
		@Bean
		Binding userCreatedEventBinding(Queue incrementCommandQueue, Exchange exchange, MessagingConfigurationProperties properties) {
			return BindingBuilder
				.bind(incrementCommandQueue)
				.to(exchange)
				.with(properties.getRoutingKey().getAchievement().getCommand().getIncrement())
				.noargs();
		}
		
	}
	
	@Configuration(proxyBeanMethods = false)
	public static class ConnectionAMQPConfiguration {
		
		@Bean
		Queue incrementCommandQueue(MessagingConfigurationProperties properties) {
			return QueueBuilder
				.durable(properties.getQueue().getAchievement().getCommand().getIncrement())
				.build();
		}
		
	}
	
	@Primary
	@Bean
	Jackson2JsonMessageConverter producerJackson2MessageConverter(ObjectMapper objectMapper) {
		return new Jackson2JsonMessageConverter(objectMapper);
	}
	
}