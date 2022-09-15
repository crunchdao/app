package com.crunchdao.app.service.registration.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Component
@ConfigurationProperties("app.messaging")
public class MessagingConfigurationProperties {
	
	private Exchange exchange = new Exchange();
	private RoutingKey routingKey = new RoutingKey();
	
	@Data
	@Accessors(chain = true)
	public static class Exchange {
		
		private String registration;
		
	}
	
	@Data
	@Accessors(chain = true)
	public static class RoutingKey {
		
		private Registration registration;
		
		@Data
		@Accessors(chain = true)
		public static class Registration {
			
			private Event event;
			
			@Data
			@Accessors(chain = true)
			public static class Event {
				
				private String registered;
				private String resigned;
				
			}
			
		}
		
	}
	
}