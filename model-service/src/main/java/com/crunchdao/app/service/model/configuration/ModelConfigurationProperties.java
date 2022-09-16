package com.crunchdao.app.service.model.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "app.model")
public class ModelConfigurationProperties {
	
	private Long limit;
	
}