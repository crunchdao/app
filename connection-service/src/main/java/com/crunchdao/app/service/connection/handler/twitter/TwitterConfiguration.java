package com.crunchdao.app.service.connection.handler.twitter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.crunchdao.app.service.connection.store.PKCEChallengeStore;
import com.crunchdao.app.service.connection.store.MemoryPKCEChallengeStore;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ConditionalOnProperty(prefix = TwitterConfigurationProperties.PREFIX, name = "enabled", havingValue = "true")
@Import(TwitterConfigurationProperties.class)
public class TwitterConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	PKCEChallengeStore pkceChallengeStore() {
		return new MemoryPKCEChallengeStore();
	}
	
	@Bean
	TwitterConnectionHandler twitterConnectionHandler(ObjectMapper objectMapper, TwitterConfigurationProperties properties, PKCEChallengeStore pkceChallengeStore) {
		return new TwitterConnectionHandler(objectMapper, properties, pkceChallengeStore);
	}
	
}