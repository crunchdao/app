package com.crunchdao.app.service.game.configuration;

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
	
	@Configuration(proxyBeanMethods = false)
	public static class UserAMQPConfiguration {
		
		@Bean
		Exchange userExchange(MessagingConfigurationProperties properties) {
			return ExchangeBuilder
				.topicExchange(properties.getExchange().getUser())
				.suppressDeclaration()
				.build();
		}
		
		@Bean
		Queue userCreatedEventQueue(MessagingConfigurationProperties properties) {
			return QueueBuilder
				.durable(properties.getQueue().getUser().getEvent().getCreated())
				.build();
		}
		
		@Bean
		Binding userCreatedEventBinding(Queue userCreatedEventQueue, Exchange userExchange, MessagingConfigurationProperties properties) {
			return BindingBuilder
				.bind(userCreatedEventQueue)
				.to(userExchange)
				.with(properties.getRoutingKey().getUser().getEvent().getCreated())
				.noargs();
		}
		
	}
	
	@Configuration(proxyBeanMethods = false)
	public static class ConnectionAMQPConfiguration {
		
		@Bean
		Exchange connectionExchange(MessagingConfigurationProperties properties) {
			return ExchangeBuilder
				.topicExchange(properties.getExchange().getConnection())
				.suppressDeclaration()
				.build();
		}
		
		@Bean
		Queue connectionCreatedEventQueue(MessagingConfigurationProperties properties) {
			return QueueBuilder
				.durable(properties.getQueue().getConnection().getEvent().getCreated())
				.build();
		}
		
		@Bean
		Binding connectionCreatedEventBinding(Queue connectionCreatedEventQueue, Exchange connectionExchange, MessagingConfigurationProperties properties) {
			return BindingBuilder
				.bind(connectionCreatedEventQueue)
				.to(connectionExchange)
				.with(properties.getRoutingKey().getConnection().getEvent().getCreated())
				.noargs();
		}
		
	}
	
	@Primary
	@Bean
	Jackson2JsonMessageConverter producerJackson2MessageConverter(ObjectMapper objectMapper) {
		return new Jackson2JsonMessageConverter(objectMapper);
	}
	
}