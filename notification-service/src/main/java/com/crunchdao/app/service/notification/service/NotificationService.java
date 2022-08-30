package com.crunchdao.app.service.notification.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crunchdao.app.service.notification.entity.Notification;
import com.crunchdao.app.service.notification.repository.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	
	private final NotificationRepository repository;
	private final ObjectMapper objectMapper;
	
	public Optional<Notification> findById(UUID id) {
		return repository.findById(id);
	}
	
	public Page<Notification> list(UUID userId, Pageable pageable, boolean onlyUnread) {
		if (onlyUnread) {
			return repository.findAllByUserIdAndReadAtIsNull(userId, pageable);
		}
		
		return repository.findAllByUserId(userId, pageable);
	}
	
	public Notification markAsRead(Notification notification) {
		return repository.save(notification.markAsReadNow());
	}
	
	public long markAllAsRead(UUID userId) {
		List<Notification> pending = repository.findAllByUserIdAndReadAtIsNull(userId);
		pending.forEach(Notification::markAsReadNow);
		repository.saveAll(pending);
		
		return pending.size();
	}
	
	public Notification create(UUID userId, Notification.Type type, UUID entityId, Object entity) {
		Object rawEntity = objectMapper.convertValue(entity, Object.class);
		
		return repository.save(new Notification()
			.setId(UUID.randomUUID())
			.setUserId(userId)
			.setType(type)
			.setCategory(type.getCategory())
			.setCreator(Notification.Creator.SYSTEM)
			.setCreatedAt(LocalDateTime.now())
			.setEntityId(entityId)
			.setEntity(rawEntity));
	}
	
}