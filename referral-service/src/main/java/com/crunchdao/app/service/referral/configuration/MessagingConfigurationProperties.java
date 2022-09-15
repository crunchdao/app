package com.crunchdao.app.service.referral.configuration;

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
		
		private String registration;
		private String referral;
		
	}
	
	@Data
	@Accessors(chain = true)
	public static class RoutingKey {
		
		private Registration registration;
		private Referral referral;
		
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
		
		@Data
		@Accessors(chain = true)
		public static class Referral {
			
			private Event event;
			
			@Data
			@Accessors(chain = true)
			public static class Event {
				
				private String created;
				private String validated;
				
			}
			
		}
		
	}
	
	@Data
	@Accessors(chain = true)
	public static class Queue {
		
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