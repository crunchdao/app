package com.crunchdao.app.service.referral.service;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.crunchdao.app.service.referral.entity.ReferralCode;
import com.crunchdao.app.service.referral.repository.ReferralCodeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReferralCodeService {
	
	private final ReferralCodeRepository repository;
	
	public Optional<ReferralCode> findByUserId(UUID userId) {
		return repository.findById(userId);
	}
	
	public Optional<ReferralCode> findByValue(String code) {
		return repository.findByValue(code);
	}
	
	public ReferralCode create(UUID userId) {
		String value = nextValidCode();
		
		return repository.saveAndFlush(new ReferralCode()
			.setUserId(userId)
			.setValue(value)
			.setEnabled(true));
	}
	
	public String nextValidCode() {
		String value;
		do {
			value = RandomStringUtils.randomAlphanumeric(10);
		} while (repository.existsByValue(value));
		
		return value;
	}
	
}