package com.crunchdao.app.service.avatar.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.crunchdao.app.service.avatar.exception.ImageConversionException;
import com.j256.simplemagic.ContentInfoUtil;
import com.j256.simplemagic.ContentType;

class JavaxImageConversionServiceTest {
	
	ContentInfoUtil contentInfoUtil;
	JavaxImageConversionService service;
	
	@BeforeEach
	void setUp() {
		this.contentInfoUtil = new ContentInfoUtil();
		this.service = new JavaxImageConversionService();
	}
	
	@Test
	void convertToPng() throws IOException {
		Resource resource = new ClassPathResource("buse.png");
		
		byte[] bytes = service.convertToPng(readAll(resource));
		
		assertNotNull(bytes);
		assertEquals(ContentType.PNG, contentInfoUtil.findMatch(bytes).getContentType());
	}
	
	@Test
	void convertToPng_invalid() {
		Resource resource = new ClassPathResource("beep.mp3");
		
		assertThrows(ImageConversionException.class, () -> {
			service.convertToPng(readAll(resource));
		});
	}
	
	@Test
	void convert_mp4() {
		final String extension = "mp4";
		Resource resource = new ClassPathResource("buse.png");
		
		var exception = assertThrows(ImageConversionException.class, () -> {
			service.convert(extension, readAll(resource));
		});
		
		var cause = exception.getCause();
		assertEquals(IllegalStateException.class, cause.getClass());
		assertEquals("no writter available for extension: " + extension, cause.getMessage());
	}
	
	public static byte[] readAll(Resource resource) throws IOException {
		try (InputStream inputStream = resource.getInputStream()) {
			return inputStream.readAllBytes();
		}
	}
	
}