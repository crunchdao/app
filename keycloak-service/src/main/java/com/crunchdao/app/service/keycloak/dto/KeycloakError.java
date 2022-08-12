package com.crunchdao.app.service.keycloak.dto;

import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakError {
	
	@JsonProperty("errorMessage")
	private String message;
	
	@JsonAnySetter
	@JsonAnyGetter
	private Map<String, Object> extra;
	
	public boolean is(String message) {
		return Objects.equals(this.message, message);
	}
	
	public interface Messages {
		
		public static final String SAME_EMAIL = "User exists with same email";
		public static final String SAME_USERNAME = "User exists with same username";
		
	}
	
}