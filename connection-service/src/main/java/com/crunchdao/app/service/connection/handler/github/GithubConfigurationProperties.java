package com.crunchdao.app.service.connection.handler.github;

import java.util.Base64;

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
	
	public String toBasic() {
		String credentials = String.format("%s:%s", clientId, clientSecret);
		String encoded = new String(Base64.getEncoder().encode(credentials.getBytes()));
		
		return String.format("Basic %s", encoded);
	}
	
}