package com.crunchdao.app.service.connection.handler;

import java.io.IOException;
import java.util.Map;

import org.springframework.cloud.sleuth.annotation.NewSpan;

public abstract class AbstractConnectionHandler implements ConnectionHandler {

	@NewSpan
	@Override
	public ConnectionIdentity fetchIdentity(HandlerContext context, String code) throws Exception {
		String accessToken = fetchAccessToken(context, code);
		Map<String, Object> userProperties = fetchUser(accessToken);
		revokeToken(accessToken);
		
		return toConnectionIdentity(userProperties);
	}

	public abstract String fetchAccessToken(HandlerContext context, String code) throws IOException;

	public abstract Map<String, Object> fetchUser(String accessToken) throws IOException;
	
	public abstract void revokeToken(String accessToken) throws IOException;

	public abstract ConnectionIdentity toConnectionIdentity(Map<String, Object> userProperties);
	
}