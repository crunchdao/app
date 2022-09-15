package com.crunchdao.app.service.referral.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "referral_codes")
public class ReferralCode {
	
	@Id
	@Type(type = "uuid-char")
	private UUID userId;
	
	@Audited
	@Column(nullable = false, unique = true)
	private String value;
	
	@Column(nullable = false)
	private boolean enabled;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
}