package com.crunchdao.app.service.notification.configuration;

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
	public static class AchievementAMQPConfiguration {
		
		@Bean
		Exchange achievementExchange(MessagingConfigurationProperties properties) {
			return ExchangeBuilder
				.topicExchange(properties.getExchange().getAchievement())
				.suppressDeclaration()
				.build();
		}
		
		@Bean
		Queue achievementUnlockedEventQueue(MessagingConfigurationProperties properties) {
			return QueueBuilder
				.durable(properties.getQueue().getAchievement().getEvent().getUnlocked())
				.build();
		}
		
		@Bean
		Binding achievementUnlockedEventBinding(Queue achievementUnlockedEventQueue, Exchange achievementExchange, MessagingConfigurationProperties properties) {
			return BindingBuilder
				.bind(achievementUnlockedEventQueue)
				.to(achievementExchange)
				.with(properties.getRoutingKey().getAchievement().getEvent().getUnlocked())
				.noargs();
		}
		
	}
	
	@Primary
	@Bean
	Jackson2JsonMessageConverter producerJackson2MessageConverter(ObjectMapper objectMapper) {
		return new Jackson2JsonMessageConverter(objectMapper);
	}
	
}