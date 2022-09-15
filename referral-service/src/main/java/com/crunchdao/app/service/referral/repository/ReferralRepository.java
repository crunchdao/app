package com.crunchdao.app.service.referral.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.crunchdao.app.service.referral.entity.Referral;

public interface ReferralRepository extends JpaRepository<Referral, UUID> {

	Page<Referral> findAllByReferrerId(UUID referrerId, Pageable pageable);

	List<Referral> findAllByUserIdAndValidatedFalse(UUID userId);
	
}