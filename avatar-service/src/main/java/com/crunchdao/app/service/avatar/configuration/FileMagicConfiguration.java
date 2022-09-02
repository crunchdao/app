package com.crunchdao.app.service.avatar.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.j256.simplemagic.ContentInfoUtil;

@Configuration
public class FileMagicConfiguration {
	
	/* should be enough for all kind of images */
	public static final int FILE_READ_SIZE = 1024;
	
	@Bean
	ContentInfoUtil contentInfoUtil() {
		ContentInfoUtil contentInfoUtil = new ContentInfoUtil();
		contentInfoUtil.setFileReadSize(FILE_READ_SIZE);
		
		return contentInfoUtil;
	}
	
}