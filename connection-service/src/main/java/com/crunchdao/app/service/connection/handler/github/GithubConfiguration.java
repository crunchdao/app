package com.crunchdao.app.service.connection.handler.github;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ConditionalOnProperty(prefix = GithubConfigurationProperties.PREFIX, name = "enabled", havingValue = "true")
@Import(GithubConfigurationProperties.class)
public class GithubConfiguration {
	
	@Bean
	GithubConnectionHandler githubConnectionHandler(ObjectMapper objectMapper, GithubConfigurationProperties properties) {
		return new GithubConnectionHandler(objectMapper, properties);
	}
	
}