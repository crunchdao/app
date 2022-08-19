package com.crunchdao.app.service.connection.configuration;

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
			.topicExchange(properties.getExchange().getConnection())
			.build();
	}
	
	@Bean
	Exchange userExchange(MessagingConfigurationProperties properties) {
		return ExchangeBuilder
			.topicExchange(properties.getExchange().getUser())
			.suppressDeclaration()
			.build();
	}
	
	@Bean
	Queue userDeletedEventQueue(MessagingConfigurationProperties properties) {
		return QueueBuilder
			.durable(properties.getQueue().getUser().getEvent().getDeleted())
			.build();
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