package com.crunchdao.app.service.referral.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.Persistable;

import com.crunchdao.app.service.referral.dto.ReferralDto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "referrals", indexes = {
	@Index(columnList = "userId", unique = true),
	@Index(columnList = "referrerId"),
})
public class Referral implements Persistable<UUID> {
	
	@Id
	@Type(type = "uuid-char")
	private UUID userId;
	
	@Column(updatable = false, nullable = false)
	@Type(type = "uuid-char")
	private UUID referrerId;
	
	@Column(updatable = false)
	private String code;
	
	@Column(nullable = false)
	private boolean validated;
	
	@Column
	private LocalDateTime validatedAt;
	
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Override
	public UUID getId() {
		return userId;
	}
	
	@Override
	public boolean isNew() {
		return true;
	}

	public Referral markAsValidated(LocalDateTime at) {
		if (!validated) {
			validated = true;
			validatedAt = at;
		}
		
		return this;
	}
	
	public ReferralDto toDto() {
		return new ReferralDto()
			.setUserId(userId)
			.setCode(code)
			.setValidated(validated)
			.setValidatedAt(validatedAt)
			.setCreatedAt(createdAt);
	}
	
}