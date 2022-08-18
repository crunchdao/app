package com.crunchdao.app.service.user.configuration;

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
		
		private String user;
		
	}
	
	@Data
	@Accessors(chain = true)
	public static class RoutingKey {
		
		private User user = new User();
		
		@Data
		@Accessors(chain = true)
		public static class User {
			
			private Event event = new Event();
			
			@Data
			@Accessors(chain = true)
			public static class Event {
				
				private String created;
				private String deleted;
				
			}
			
		}
		
	}
	
}