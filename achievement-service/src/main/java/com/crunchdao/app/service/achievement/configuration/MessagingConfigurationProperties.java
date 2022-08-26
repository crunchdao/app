package com.crunchdao.app.service.achievement.configuration;

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
			
			private Command command;
			
			@Data
			@Accessors(chain = true)
			public static class Command {
				
				private String increment;
				
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
			
			private Command command;
			
			@Data
			@Accessors(chain = true)
			public static class Command {
				
				private String increment;
				
			}
			
		}
		
	}
	
}