package com.crunchdao.app.common.security.role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public interface CommonRoles {
	
	public static final String PREFIX = "ROLE_";
	
	public interface Name {
		
		public static final String USER = "user";
		public static final String ADMIN = "admin";
		public static final String SERVICE = "service";
		
	}
	
	public static final String USER = PREFIX + Name.USER;
	public static final String ADMIN = PREFIX + Name.ADMIN;
	public static final String SERVICE = PREFIX + Name.SERVICE;
	
	public interface Authority {
		
		public static final GrantedAuthority USER = new SimpleGrantedAuthority(CommonRoles.USER);
		public static final GrantedAuthority ADMIN = new SimpleGrantedAuthority(CommonRoles.ADMIN);
		public static final GrantedAuthority SERVICE = new SimpleGrantedAuthority(CommonRoles.SERVICE);
		
	}
	
}