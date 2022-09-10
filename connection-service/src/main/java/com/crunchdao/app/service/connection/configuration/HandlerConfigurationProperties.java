package com.crunchdao.app.service.connection.configuration;

import java.util.TreeMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Component
@ConfigurationProperties("app.connection.handler")
public class HandlerConfigurationProperties {
	
	private CaseInsensitiveMap<String> names = new CaseInsensitiveMap<>();
	
	public static class CaseInsensitiveMap<V> extends TreeMap<String, V> {
		
		private static final long serialVersionUID = 1L;
		
		public CaseInsensitiveMap() {
			super(String.CASE_INSENSITIVE_ORDER);
		}
		
	}
	
}