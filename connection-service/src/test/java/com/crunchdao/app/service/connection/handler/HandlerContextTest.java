package com.crunchdao.app.service.connection.handler;

import java.util.UUID;

public class HandlerContextTest {
	
	public static final String DEFAULT_REDIRECTION_URL = "https://google.com";
	
	public static HandlerContext createSimple() {
		return createSimple(UUID.randomUUID());
	}
	
	public static HandlerContext createSimple(UUID userId) {
		return new HandlerContext() {
			
			@Override
			public UUID getUserId() {
				return userId;
			}
			
			@Override
			public String getRedirectionUrl() {
				return DEFAULT_REDIRECTION_URL;
			}
			
		};
	}
	
}