package com.crunchdao.app.common.security.role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public interface CommonRoles {
	
	public static final String PREFIX = "ROLE_";
	
	public static final String USER = PREFIX + "user";
	public static final String ADMIN = PREFIX + "admin";
	
	public static final String SERVICE = PREFIX + "service";
	
	public static final GrantedAuthority SERVICE_AUTHORITY = new SimpleGrantedAuthority(SERVICE);
	
}