package com.crunchdao.app.service.referral.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crunchdao.app.service.referral.entity.ReferralCode;

public interface ReferralCodeRepository extends JpaRepository<ReferralCode, UUID> {

	Optional<ReferralCode> findByValue(String value);
	
	boolean existsByValue(String value);
	
}