package com.crunchdao.app.service.connection.configuration;

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
		
		private String user;
		private String connection;
		
	}
	
	@Data
	@Accessors(chain = true)
	public static class RoutingKey {
		
		private Connection connection;
		private User user;
		
		@Data
		@Accessors(chain = true)
		public static class Connection {
			
			private Event event;
			
			@Data
			@Accessors(chain = true)
			public static class Event {
				
				private String created;
				private String deleted;
				
			}
			
		}
		
		@Data
		@Accessors(chain = true)
		public static class User {
			
			private Event event;
			
			@Data
			@Accessors(chain = true)
			public static class Event {
				
				private String deleted;
				
			}
			
		}
		
	}
	
	@Data
	@Accessors(chain = true)
	public static class Queue {
		
		private User user;
		
		@Data
		@Accessors(chain = true)
		public static class User {
			
			private Event event;
			
			@Data
			@Accessors(chain = true)
			public static class Event {
				
				private String deleted;
				
			}
			
		}
		
	}
	
}