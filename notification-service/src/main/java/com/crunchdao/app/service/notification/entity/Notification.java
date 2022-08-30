package com.crunchdao.app.service.notification.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

@Document(collection = "notifications")
@Data
@Accessors(chain = true)
public class Notification {
	
	@Id
	private UUID id;
	
	@Field
	@Indexed
	private UUID userId;
	
	@Field
	@Indexed
	private Category category;
	
	@Field
	@Indexed
	private Type type;
	
	@Field
	private Creator creator;
	
	@Field
	private Object entityId;
	
	@Field
	private Object entity;
	
	@Field
	private LocalDateTime createdAt;
	
	@Field
	private LocalDateTime readAt;
	
	public Notification markAsReadNow() {
		return setReadAt(LocalDateTime.now());
	}
	
	public enum Category {
		
		ACHIEVEMENT;
	
	}
	
	@AllArgsConstructor
	@Getter
	public enum Type {
		
		ACHIEVEMENT_UNLOCKED(Category.ACHIEVEMENT);
		
		private final Category category;
		
	}
	
	public enum Creator {
		
		SYSTEM,
		STAFF;
	
	}
	
}