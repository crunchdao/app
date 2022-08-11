package com.crunchdao.app.common.security.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.crunchdao.app.common.security.filter.HeaderAuthenticationFilter;
import com.crunchdao.app.common.security.role.CommonRoles;
import com.crunchdao.app.common.security.unauthorized.UnauthorizedEntryPoint;

@SuppressWarnings("deprecation")
@Import(UnauthorizedEntryPoint.class)
public class BaseApplicationSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	protected UnauthorizedEntryPoint unauthorizedEntryPoint;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		http.csrf().disable();
		http.httpBasic().disable();
		http.headers().frameOptions().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint);
		http.authorizeHttpRequests().antMatchers("/swagger-ui/**").permitAll();
		
		configureFilters(http);
	}
	
	protected void configureFilters(HttpSecurity http) {
		http.addFilterBefore(createHeaderAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	protected HeaderAuthenticationFilter createHeaderAuthenticationFilter() {
		Collection<? extends GrantedAuthority> authorities = getServiceAuthorities();
		
		if (!authorities.contains(CommonRoles.Authority.SERVICE)) {
			List<GrantedAuthority> authoritiesWithRole = new ArrayList<>(authorities);
			authoritiesWithRole.add(CommonRoles.Authority.SERVICE);
			
			authorities = authoritiesWithRole;
		}
		
		return new HeaderAuthenticationFilter(authorities);
	}
	
	protected Collection<? extends GrantedAuthority> getServiceAuthorities() {
		return Collections.emptySet();
	}
	
}