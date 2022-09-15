package com.crunchdao.app.service.referral.configuration;

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
	public static class ReferralAMQPConfiguration {
		
		@Bean
		Exchange referralExchange(MessagingConfigurationProperties properties) {
			return ExchangeBuilder
				.topicExchange(properties.getExchange().getReferral())
				.build();
		}
		
	}
	
	@Configuration(proxyBeanMethods = false)
	public static class RegistrationAMQPConfiguration {
		
		@Bean
		Exchange registrationExchange(MessagingConfigurationProperties properties) {
			return ExchangeBuilder
				.topicExchange(properties.getExchange().getRegistration())
				.suppressDeclaration()
				.build();
		}
		
		@Bean
		Queue registrationRegisteredEventQueue(MessagingConfigurationProperties properties) {
			return QueueBuilder
				.durable(properties.getQueue().getRegistration().getEvent().getRegistered())
				.build();
		}
		
		@Bean
		Queue registrationResignedEventQueue(MessagingConfigurationProperties properties) {
			return QueueBuilder
				.durable(properties.getQueue().getRegistration().getEvent().getResigned())
				.build();
		}
		
		@Bean
		Binding registrationRegisteredEventBinding(Queue registrationRegisteredEventQueue, Exchange registrationExchange, MessagingConfigurationProperties properties) {
			return BindingBuilder
				.bind(registrationRegisteredEventQueue)
				.to(registrationExchange)
				.with(properties.getRoutingKey().getRegistration().getEvent().getRegistered())
				.noargs();
		}
		
		@Bean
		Binding registrationResignedEventBinding(Queue registrationResignedEventQueue, Exchange registrationExchange, MessagingConfigurationProperties properties) {
			return BindingBuilder
				.bind(registrationResignedEventQueue)
				.to(registrationExchange)
				.with(properties.getRoutingKey().getRegistration().getEvent().getResigned())
				.noargs();
		}
		
	}
	
	@Primary
	@Bean
	Jackson2JsonMessageConverter producerJackson2MessageConverter(ObjectMapper objectMapper) {
		return new Jackson2JsonMessageConverter(objectMapper);
	}
	
}