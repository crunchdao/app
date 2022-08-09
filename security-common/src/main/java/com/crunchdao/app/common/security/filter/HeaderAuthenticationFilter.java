package com.crunchdao.app.common.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.crunchdao.app.common.security.SecurityHttpHeaders;
import com.crunchdao.app.common.security.authentication.AuthenticationType;
import com.crunchdao.app.common.security.token.ServiceAuthenticationToken;
import com.crunchdao.app.common.security.token.UserAuthenticationToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeaderAuthenticationFilter extends OncePerRequestFilter {
	
	private final Collection<? extends GrantedAuthority> serviceAuthorities;
	
	public HeaderAuthenticationFilter() {
		this.serviceAuthorities = AuthorityUtils.NO_AUTHORITIES;
	}
	
	public HeaderAuthenticationFilter(Collection<? extends GrantedAuthority> serviceAuthorities) {
		this.serviceAuthorities = new ArrayList<>(serviceAuthorities);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Authentication authentication = authenticate(request);
		
		if (authentication != null) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response);
	}
	
	public Authentication authenticate(HttpServletRequest request) {
		AuthenticationType type = AuthenticationType.parse(request.getHeader(SecurityHttpHeaders.AUTHENTICATION_TYPE));
		if (type == null) {
			return null;
		}
		
		switch (type) {
			case SERVICE: {
				return authenticationService(request);
			}
			
			case USER: {
				return authenticateUser(request);
			}
			
			default: {
				log.warn("Unsupported authentication type: {}", type);
				return null;
			}
		}
	}
	
	public ServiceAuthenticationToken authenticationService(HttpServletRequest request) {
		String rawUserId = request.getHeader(SecurityHttpHeaders.USER_ID);
		String serviceName = request.getHeader(SecurityHttpHeaders.SERVICE_NAME);
		
		try {
			UUID userId = UUID.fromString(rawUserId);
			
			if (StringUtils.hasText(serviceName)) {
				return new ServiceAuthenticationToken(serviceName, userId, serviceAuthorities);
			}
		} catch (Exception exception) {
			return null;
		}
		
		return null;
	}
	
	public static UserAuthenticationToken authenticateUser(HttpServletRequest request) {
		String rawUserId = request.getHeader(SecurityHttpHeaders.USER_ID);
		String rawAuthorities = request.getHeader(SecurityHttpHeaders.AUTHORITIES);
		
		try {
			UUID userId = UUID.fromString(rawUserId);
			List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(rawAuthorities);
			
			return new UserAuthenticationToken(userId, authorities);
		} catch (Exception exception) {
			return null;
		}
	}
	
	public static AuthenticationType extractAuthenticationType(HttpServletRequest request) {
		return AuthenticationType.parse(request.getHeader(SecurityHttpHeaders.AUTHENTICATION_TYPE));
	}
	
}