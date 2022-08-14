package com.crunchdao.app.service.apikey.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Component
@Validated
@ConfigurationProperties(prefix = "api-key")
public class ApiKeyConfigurationProperties {
	
	@NotEmpty
	private List<String> allowedScopes = new ArrayList<>();
	
}