package com.crunchdao.app.service.follow.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.crunchdao.app.service.follow.entity.Follow;

public interface FollowRepository extends JpaRepository<Follow, Follow.CompositeId> {

	Page<Follow> findAllByIdUserId(UUID userId, Pageable pageable);
	
	Page<Follow> findAllByIdPeerId(UUID peerId, Pageable pageable);
	
	long countByIdUserId(UUID userId);
	
	long countByIdPeerId(UUID peerId);
	
	long removeById(Follow.CompositeId id);
	
}