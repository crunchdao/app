package com.crunchdao.app.service.connection.handler;

import java.util.UUID;

public interface HandlerContext {
	
	UUID getUserId();
	
	String getRedirectionUrl();
	
}