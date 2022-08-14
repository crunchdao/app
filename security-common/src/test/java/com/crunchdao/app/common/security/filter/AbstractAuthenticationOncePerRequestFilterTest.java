package com.crunchdao.app.common.security.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;

import com.crunchdao.app.common.security.SecurityHttpHeaders;

class AbstractAuthenticationOncePerRequestFilterTest {
	
	@Test
	void extractApiKey_HttpServletRequest() {
		final String value = "hello";
		
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		Mockito.when(request.getHeader(captor.capture())).thenReturn(SecurityHttpHeaders.API_KEY_PREFIX + " " + value);
		
		assertThat(AbstractAuthenticationOncePerRequestFilter.extractApiKey(request)).hasValue(value);
		assertEquals(HttpHeaders.AUTHORIZATION, captor.getValue());
	}
	
	@Test
	void extractApiKey_String() {
		assertThat(AbstractAuthenticationOncePerRequestFilter.extractApiKey((String) null)).isEmpty();
		assertThat(AbstractAuthenticationOncePerRequestFilter.extractApiKey("")).isEmpty();
		assertThat(AbstractAuthenticationOncePerRequestFilter.extractApiKey("X")).isEmpty();
		assertThat(AbstractAuthenticationOncePerRequestFilter.extractApiKey(SecurityHttpHeaders.API_KEY_PREFIX)).isEmpty();
		assertThat(AbstractAuthenticationOncePerRequestFilter.extractApiKey("Invalid-Prefix hello")).isEmpty();
		
		assertThat(AbstractAuthenticationOncePerRequestFilter.extractApiKey(SecurityHttpHeaders.API_KEY_PREFIX + " hello")).hasValue("hello");
		assertThat(AbstractAuthenticationOncePerRequestFilter.extractApiKey(SecurityHttpHeaders.API_KEY_PREFIX + " hello world")).hasValue("hello world");
	}
	
	@Test
	void formatApiKeyAuthorizationHeader() {
		assertEquals(SecurityHttpHeaders.API_KEY_PREFIX + " hello", AbstractAuthenticationOncePerRequestFilter.formatApiKeyAuthorizationHeader("hello"));
	}
	
}