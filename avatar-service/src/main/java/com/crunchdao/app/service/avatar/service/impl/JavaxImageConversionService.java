package com.crunchdao.app.service.avatar.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.crunchdao.app.service.avatar.exception.ImageConversionException;
import com.crunchdao.app.service.avatar.service.ImageConversionService;

@Service
public class JavaxImageConversionService implements ImageConversionService {
	
	@Override
	public byte[] convert(String extension, byte[] bytes) throws ImageConversionException {
		try {
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
			ExposedByteArrayOutputStream output = new ExposedByteArrayOutputStream(bytes.length);
			
			if (!ImageIO.write(bufferedImage, extension, output)) {
				throw new IllegalStateException("no writter available for extension: %s".formatted(extension));
			}
			
			return output.getBuffer();
		} catch (IOException exception) {
			throw new ImageConversionException(exception);
		}
	}
	
	static class ExposedByteArrayOutputStream extends ByteArrayOutputStream {
		
		public ExposedByteArrayOutputStream(int size) {
			super(size);
		}
		
		public byte[] getBuffer() {
			return buf;
		}
		
	}
	
}