package com.crunchdao.app.service.connection.handler.twitter;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.experimental.Accessors;

@Validated
@Data
@Accessors(chain = true)
@ConfigurationProperties(TwitterConfigurationProperties.PREFIX)
public class TwitterConfigurationProperties {
	
	public static final String PREFIX = "twitter";

	@NotNull
	private String clientId;
	
}