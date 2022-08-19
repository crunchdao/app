package com.crunchdao.app.service.connection.handler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionIdentity {
	
	private final String subject;
	private final String handle;
	private final String username;
	private final String profileUrl;
	
}