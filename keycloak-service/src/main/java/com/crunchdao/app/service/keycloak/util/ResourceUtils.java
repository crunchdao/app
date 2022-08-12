package com.crunchdao.app.service.keycloak.util;

import java.net.URI;
import java.util.UUID;

import javax.ws.rs.core.Response;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResourceUtils {
	
	public static UUID extractId(Response response, boolean strict) {
		return extractId(response.getLocation(), strict);
	}
	
	public static UUID extractId(URI uri, boolean strict) {
		if (uri == null) {
			if (strict) {
				throw new IllegalArgumentException("uri is null");
			}
			
			return null;
		}
		
		try {
			String extracted = uri.getPath().replaceFirst("^.*\\/", "");
			
			return UUID.fromString(extracted);
		} catch (Exception exception) {
			if (strict) {
				throw exception;
			}
		}
		
		return null;
	}
	
}