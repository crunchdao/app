package com.crunchdao.app.service.connection.handler.fake;

import com.crunchdao.app.service.connection.exception.BadInputException;
import com.crunchdao.app.service.connection.handler.ConnectionHandler;
import com.crunchdao.app.service.connection.handler.ConnectionIdentity;
import com.crunchdao.app.service.connection.handler.ConnectionIdentityTest;
import com.crunchdao.app.service.connection.handler.HandlerContext;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class FakeConnectionHandler implements ConnectionHandler {
	
	private final String type;
	private final boolean valid;
	
	@Override
	public String generateUrl(HandlerContext context) {
		return String.format("http://google.com/auth/authorize?user_id=%s&redirect_url=%s", context.getUserId(), context.getRedirectionUrl());
	}
	
	@Override
	public ConnectionIdentity fetchIdentity(HandlerContext context, String code) throws Exception {
		if (!valid) {
			throw BadInputException.invalidCode();
		}
		
		return ConnectionIdentityTest.createRandom();
	}
	
}