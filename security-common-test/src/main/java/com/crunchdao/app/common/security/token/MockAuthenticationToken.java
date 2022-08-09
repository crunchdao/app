package com.crunchdao.app.common.security.token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.crunchdao.app.common.security.SecurityHttpHeaders;
import com.crunchdao.app.common.security.authentication.AuthenticationType;
import com.crunchdao.app.common.security.role.CommonRoles;

public class MockAuthenticationToken {
	
	public static final String[] NO_AUTHORITIES = new String[0];
	public static final String DUMMY_SERVICE_NAME = "dummy-service";
	
	public static RequestPostProcessor user() {
		return user(UUID.randomUUID());
	}
	
	public static RequestPostProcessor user(UUID userId) {
		return user(userId, NO_AUTHORITIES);
	}
	
	public static RequestPostProcessor user(UUID userId, String... authorities) {
		List<String> authoritiesWithRole = new ArrayList<>(1 + authorities.length);
		
		authoritiesWithRole.add(CommonRoles.USER);
		Collections.addAll(authoritiesWithRole, authorities);
		
		return (request) -> {
			setAuthenticationType(request, AuthenticationType.USER);
			setUserId(request, userId);
			setAuthorities(request, authoritiesWithRole.toArray(String[]::new));
			
			return request;
		};
	}
	
	public static RequestPostProcessor service() {
		return service(DUMMY_SERVICE_NAME, UUID.randomUUID());
	}
	
	public static RequestPostProcessor service(UUID userId) {
		return service(DUMMY_SERVICE_NAME, userId);
	}
	
	public static RequestPostProcessor service(String serviceName, UUID userId) {
		return (request) -> {
			setAuthenticationType(request, AuthenticationType.SERVICE);
			setServiceName(request, serviceName);
			setUserId(request, userId);
			
			return request;
		};
	}
	
	private static void setAuthenticationType(MockHttpServletRequest request, AuthenticationType authenticationType) {
		request.addHeader(SecurityHttpHeaders.AUTHENTICATION_TYPE, authenticationType.name());
	}
	
	private static void setUserId(MockHttpServletRequest request, UUID userId) {
		request.addHeader(SecurityHttpHeaders.USER_ID, userId.toString());
	}
	
	private static void setServiceName(MockHttpServletRequest request, String serviceName) {
		request.addHeader(SecurityHttpHeaders.SERVICE_NAME, serviceName);
	}
	
	private static void setAuthorities(MockHttpServletRequest request, String[] authorities) {
		if (authorities.length != 0) {
			request.addHeader(SecurityHttpHeaders.AUTHORITIES, String.join(",", authorities));
		}
	}
	
}