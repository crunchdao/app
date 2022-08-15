package com.crunchdao.app.service.apikey.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.crunchdao.app.service.apikey.dto.ScopeDto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Component
@Validated
@ConfigurationProperties(prefix = "api-key")
public class ApiKeyConfigurationProperties {
	
	@NotEmpty
	private List<ScopeDto> allowedScopes = new ArrayList<>();
	
	public ApiKeyConfigurationProperties setAllowedScopesFromString(List<String> scopes) {
		return setAllowedScopes(scopes.stream().map((scope) -> new ScopeDto().setName(scope)).toList());
	}
	
	public List<String> getAllowedScopesAsString() {
		return allowedScopes.stream().map(ScopeDto::getName).toList();
	}
	
}