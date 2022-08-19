package com.crunchdao.app.service.connection.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import com.crunchdao.app.service.connection.dto.ConnectionDto;
import com.crunchdao.app.service.connection.handler.ConnectionIdentity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "connections", uniqueConstraints = {
	@UniqueConstraint(columnNames = { "userId", "type" }),
}, indexes = {
	@Index(columnList = "userId"),
	@Index(columnList = "type"),
})
@Data
@Accessors(chain = true)
public class Connection {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@JsonIgnore
	@Column(updatable = false, nullable = false)
	@Type(type = "uuid-char")
	private UUID userId;
	
	@Column(nullable = false, updatable = false)
	private String type;
	
	@Column(nullable = false)
	@JsonIgnore
	/** User ID of the website. */
	private String subject;
	
	@Audited
	@Column
	/** Social accessor. */
	private String handle;
	
	@Audited
	@Column
	/** Displayable name. */
	private String username;
	
	@Audited
	@Column
	private String profileUrl;
	
	@Column(name = "public", nullable = false)
	private boolean isPublic;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	public Connection mergeIdentity(ConnectionIdentity identity) {
		return this
			.setSubject(identity.getSubject())
			.setHandle(identity.getHandle())
			.setUsername(identity.getUsername())
			.setProfileUrl(identity.getProfileUrl());
	}
	
	public ConnectionDto toDto() {
		return new ConnectionDto()
			.setUserId(userId)
			.setType(type)
			.setHandle(handle)
			.setUsername(username)
			.setProfileUrl(profileUrl)
			.setIsPublic(isPublic)
			.setCreatedAt(createdAt)
			.setUpdatedAt(updatedAt);
	}
	
}