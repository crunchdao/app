package com.crunchdao.app.service.apikey.plain;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;

import com.crunchdao.app.common.security.SecurityHttpHeaders;
import com.crunchdao.app.service.apikey.dto.ApiKeyDto;
import com.crunchdao.app.service.apikey.service.ApiKeyService;

class PlainApiKeyAuthenticationFilterTest {
	
	@Test
	void authenticate() {
		final String plain = "hello";
		
		ApiKeyService service = Mockito.mock(ApiKeyService.class);
		PlainApiKeyAuthenticationFilter filter = new PlainApiKeyAuthenticationFilter(service);
		
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(SecurityHttpHeaders.API_KEY_PREFIX + " " + plain);
		
		Mockito.when(service.findByPlain(plain)).thenReturn(Optional.empty());
		assertNull(filter.authenticate(request));
		
		Mockito.when(service.findByPlain(plain)).thenReturn(Optional.of(new ApiKeyDto()));
		assertNotNull(filter.authenticate(request));
	}
	
}