package com.crunchdao.app.service.connection.handler.discord;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ConditionalOnProperty(prefix = DiscordConfigurationProperties.PREFIX, name = "enabled", havingValue = "true")
@Import(DiscordConfigurationProperties.class)
public class DiscordConfiguration {
	
	@Bean
	DiscordConnectionHandler discordConnectionHandler(ObjectMapper objectMapper, DiscordConfigurationProperties properties) {
		return new DiscordConnectionHandler(objectMapper, properties);
	}
	
}