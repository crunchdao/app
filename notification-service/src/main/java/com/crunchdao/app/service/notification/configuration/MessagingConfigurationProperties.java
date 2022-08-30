package com.crunchdao.app.service.notification.configuration;

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
	private Queue queue = new Queue();
	
	@Data
	@Accessors(chain = true)
	public static class Exchange {
		
		private String achievement;
		
	}
	
	@Data
	@Accessors(chain = true)
	public static class RoutingKey {
		
		private Achievement achievement;
		
		@Data
		@Accessors(chain = true)
		public static class Achievement {
			
			private Event event;
			
			@Data
			@Accessors(chain = true)
			public static class Event {
				
				private String unlocked;
				
			}
			
		}
		
	}
	
	@Data
	@Accessors(chain = true)
	public static class Queue {
		
		private Achievement achievement;
		
		@Data
		@Accessors(chain = true)
		public static class Achievement {
			
			private Event event;
			
			@Data
			@Accessors(chain = true)
			public static class Event {
				
				private String unlocked;
				
			}
			
		}
		
	}
	
}