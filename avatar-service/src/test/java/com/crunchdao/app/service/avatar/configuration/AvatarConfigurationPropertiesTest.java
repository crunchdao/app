package com.crunchdao.app.service.avatar.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.j256.simplemagic.ContentType;

class AvatarConfigurationPropertiesTest {
	
	@Test
	void loadSupportedContentTypeFromSPI() {
		assertThat(AvatarConfigurationProperties.loadSupportedContentTypeFromSPI())
			.isNotEmpty()
			.contains(ContentType.PNG)
			.contains(ContentType.JPEG);
	}
	
}