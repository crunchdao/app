package com.crunchdao.app.service.connection.handler;

public interface ConnectionHandler {
	
	String generateUrl(HandlerContext context);
	
	ConnectionIdentity fetchIdentity(HandlerContext context, String code) throws Exception;
	
	String getType();
	
}