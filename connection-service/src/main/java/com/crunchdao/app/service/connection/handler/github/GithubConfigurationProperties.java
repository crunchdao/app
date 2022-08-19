package com.crunchdao.app.service.connection.handler.github;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Validated
@Data
@Accessors(chain = true)
@ConfigurationProperties(GithubConfigurationProperties.PREFIX)
public class GithubConfigurationProperties {
	
	public static final String PREFIX = "github";

	@NotNull
	private String clientId;

	@NotNull
	@ToString.Exclude
	private String clientSecret;
	
}