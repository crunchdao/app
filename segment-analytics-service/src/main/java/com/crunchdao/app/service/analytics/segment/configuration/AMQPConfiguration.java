package com.crunchdao.app.service.analytics.segment.configuration;

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
	Queue userDeletedEventQueue(MessagingConfigurationProperties properties) {
		return QueueBuilder
			.durable(properties.getQueue().getUser().getEvent().getDeleted())
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
	
	@Bean
	Binding userDeletedEventBinding(Queue userDeletedEventQueue, Exchange userExchange, MessagingConfigurationProperties properties) {
		return BindingBuilder
			.bind(userDeletedEventQueue)
			.to(userExchange)
			.with(properties.getRoutingKey().getUser().getEvent().getDeleted())
			.noargs();
	}
	
	@Primary
	@Bean
	Jackson2JsonMessageConverter producerJackson2MessageConverter(ObjectMapper objectMapper) {
		return new Jackson2JsonMessageConverter(objectMapper);
	}
	
}