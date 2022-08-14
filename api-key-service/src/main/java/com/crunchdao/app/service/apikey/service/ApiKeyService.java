package com.crunchdao.app.service.apikey.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.apikey.dto.ApiKeyDto;
import com.crunchdao.app.service.apikey.entity.ApiKey;
import com.crunchdao.app.service.apikey.repository.ApiKeyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ApiKeyService {
	
	public static final int RETRY_COUNT = 5;
	
	private final ApiKeyRepository repository;
	
	public Optional<ApiKeyDto> findById(UUID id) {
		return repository.findById(id).map(ApiKey::toDto);
	}
	
	public Optional<ApiKeyDto> findByPlain(String plain) {
		return repository.findByHash(hash(plain)).map(ApiKey::toDto);
	}
	
	public PageResponse<ApiKeyDto> listForUserId(UUID userId, Pageable pageable) {
		Page<ApiKey> apiKeys = repository.findAllByUserId(userId, pageable);
		
		return new PageResponse<>(apiKeys, ApiKey::toDto);
	}
	
	public ApiKeyDto create(ApiKeyDto body, UUID userId) {
		return create(body, userId, ApiKeyService::randomString);
	}
	
	public ApiKeyDto create(ApiKeyDto body, UUID userId, Supplier<String> generator) {
		for (int n = 0; n < RETRY_COUNT; ++n) {
			String plain = generator.get();
			
			System.out.println(hash(plain));
			
			try {
				return repository.save(new ApiKey()
					.setId(UUID.randomUUID())
					.setUserId(userId)
					.setName(body.getName())
					.setDescription(body.getDescription())
					.setPlain(plain)
					.setHash(hash(plain))
					.setTruncated(truncate(plain))
					.setCreatedAt(LocalDateTime.now())
					.setUpdatedAt(LocalDateTime.now())
					.setScopes(body.getScopes()))
					.toDto();
			} catch (DuplicateKeyException exception) {
				log.warn("Collision", exception);
			}
		}
		
		throw new IllegalStateException("too much collision in a row");
	}
	
	public void delete(UUID id) {
		repository.deleteById(id);
	}
	
	public void deleteAllByUserId(UUID userId) {
		repository.deleteAllByUserId(userId);
	}
	
	public static String randomString() {
		return RandomStringUtils.randomAlphabetic(60);
	}
	
	public static String hash(String plain) {
		return DigestUtils.sha256Hex(plain);
	}
	
	public static String truncate(String plain) {
		return plain.substring(0, 4);
	}
	
}