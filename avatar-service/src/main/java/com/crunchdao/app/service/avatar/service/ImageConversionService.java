package com.crunchdao.app.service.avatar.service;

import org.springframework.stereotype.Service;

import com.crunchdao.app.service.avatar.exception.ImageConversionException;

@Service
public interface ImageConversionService {
	
	public static final String PNG = "png";
	
	default byte[] convertToPng(byte[] bytes) throws ImageConversionException {
		return convert(PNG, bytes);
	}
	
	byte[] convert(String extension, byte[] bytes) throws ImageConversionException;
	
}