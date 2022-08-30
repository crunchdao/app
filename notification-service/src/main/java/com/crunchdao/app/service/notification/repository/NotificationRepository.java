package com.crunchdao.app.service.notification.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.crunchdao.app.service.notification.entity.Notification;

public interface NotificationRepository extends MongoRepository<Notification, UUID> {
	
	Page<Notification> findAllByUserIdAndReadAtIsNull(UUID userId, Pageable pageable);
	
	Page<Notification> findAllByUserId(UUID userId, Pageable pageable);
	
	List<Notification> findAllByUserIdAndReadAtIsNull(UUID userId);
	
}