package com.crunchdao.app.service.keycloak.util;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.URI;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

class ResourceUtilsTest {
	
	@Test
	void extractId_location() {
		Response response = mock(Response.class);
		
		assertNull(ResourceUtils.extractId(response, false));
		
		verify(response, times(1)).getLocation();
	}
	
	@Test
	void extractId_uri() {
		final UUID id = UUID.randomUUID();
		
		assertEquals(id, ResourceUtils.extractId(createDummyUri(id), false));
	}
	
	@Test
	void extractId_invalidUri_notStrict() {
		assertNull(ResourceUtils.extractId(createDummyUri("invalid"), false));
	}
	
	@Test
	void extractId_invalidUri_strict() {
		assertThrows(IllegalArgumentException.class, () -> {
			ResourceUtils.extractId(createDummyUri("invalid"), true);
		});
	}
	
	@Test
	void extractId_nullUri_strict() {
		assertThrows(IllegalArgumentException.class, () -> {
			ResourceUtils.extractId((URI) null, true);
		});
	}
	
	@Test
	void extractId_nullUri_notStrict() {
		assertNull(ResourceUtils.extractId((URI) null, false));
	}
	
	public static URI createDummyUri(Object id) {
		return URI.create(String.format("/api/resources/%s", id));
	}
	
}