package com.crunchdao.app.common.security.authentication;

import org.springframework.util.StringUtils;

public enum AuthenticationType {
	
	USER,
	SERVICE;
	
	public static AuthenticationType parse(String input) {
		if (!StringUtils.hasLength(input)) {
			return null;
		}
		
		try {
			return valueOf(input.toUpperCase());
		} catch (IllegalArgumentException exception) {
			return null;
		}
	}
	
}