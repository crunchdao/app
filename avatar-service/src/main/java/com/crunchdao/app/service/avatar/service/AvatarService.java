package com.crunchdao.app.service.avatar.service;

import java.io.InputStream;
import java.util.UUID;

public interface AvatarService {
	
	void upload(InputStream inputStream, UUID userId);

	void uploadFromTemplate(UUID userId);
	
	void uploadFromFallback(UUID userId);
	
	String toUrl(UUID userId);

}