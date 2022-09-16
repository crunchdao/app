package com.crunchdao.app.service.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import com.crunchdao.app.service.model.dto.ModelDto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "models")
public class Model {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Type(type = "uuid-char")
	private UUID id;
	
	@Column(nullable = false, updatable = false)
	@Type(type = "uuid-char")
	private UUID userId;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column(nullable = false, length = 1024)
	@Lob
	private String comment;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	public ModelDto toDto(boolean includeComments) {
		return new ModelDto()
			.setId(id)
			.setUserId(userId)
			.setName(name)
			.setComment(includeComments ? comment : null)
			.setCreatedAt(createdAt)
			.setUpdatedAt(updatedAt);
	}
	
}