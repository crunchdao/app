package com.crunchdao.app.service.follow.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.Persistable;

import com.crunchdao.app.service.follow.dto.FollowDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "follows", indexes = {
	@Index(columnList = "userId"),
	@Index(columnList = "peerId"),
})
public class Follow implements Persistable<Follow.CompositeId> {
	
	@EmbeddedId
	private CompositeId id;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@Override
	public boolean isNew() {
		return true;
	}
	
	public Follow setId(UUID userId, UUID peerId) {
		return setId(id(userId, peerId));
	}
	
	public FollowDto toDto() {
		return new FollowDto()
			.setUserId(id.getUserId())
			.setPeerId(id.getPeerId())
			.setCreatedAt(createdAt);
	}
	
	@Data
	@Accessors(chain = true)
	@NoArgsConstructor
	@AllArgsConstructor
	@Embeddable
	public static class CompositeId implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		@Column(updatable = false, nullable = false)
		@Type(type = "uuid-char")
		private UUID userId;
		
		@Column(updatable = false, nullable = false)
		@Type(type = "uuid-char")
		private UUID peerId;
		
	}
	
	public static CompositeId id(UUID userId, UUID peerId) {
		return new CompositeId(userId, peerId);
	}
	
}