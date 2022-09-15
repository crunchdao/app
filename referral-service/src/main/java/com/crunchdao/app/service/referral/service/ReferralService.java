package com.crunchdao.app.service.referral.service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.referral.dto.ReferralDto;
import com.crunchdao.app.service.referral.entity.Referral;
import com.crunchdao.app.service.referral.exception.AlreadyReferredException;
import com.crunchdao.app.service.referral.exception.ReferringYourselfException;
import com.crunchdao.app.service.referral.repository.ReferralRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReferralService {
	
	private final ReferralRepository repository;
	
	public PageResponse<ReferralDto> list(UUID referrerId, Pageable pageable) {
		final Page<Referral> page = repository.findAllByReferrerId(referrerId, pageable);
		
		return new PageResponse<>(page, Referral::toDto);
	}
	
	public Referral refer(UUID userId, UUID referrerId, String code) {
		if (Objects.equals(userId, referrerId)) {
			throw new ReferringYourselfException();
		}
		
		try {
			return repository.saveAndFlush(new Referral()
				.setUserId(userId)
				.setReferrerId(referrerId)
				.setCode(code));
		} catch (DataIntegrityViolationException exception) {
			throw new AlreadyReferredException(userId, exception);
		}
	}
	
	public void validate(UUID userId, LocalDateTime at) {
		if (at == null) {
			at = LocalDateTime.now();
		}
		
		final var validatedAt = at;
		
		repository.findAllByUserIdAndValidatedFalse(userId)
			.stream()
			.map((referral) -> referral.markAsValidated(validatedAt))
			.forEach(repository::save); /* TODO: Send event */
	}
	
}