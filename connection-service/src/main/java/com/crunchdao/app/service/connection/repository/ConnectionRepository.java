package com.crunchdao.app.service.connection.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crunchdao.app.service.connection.entity.Connection;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
	
	List<Connection> findAllByUserId(UUID userId);
	
	List<Connection> findAllByUserIdAndIsPublicTrue(UUID userId);
	
	Optional<Connection> findByUserIdAndType(UUID userId, String type);
	
	long deleteByUserIdAndType(UUID userId, String type);
	
	long deleteAllByUserId(UUID userId);

	long countByUserId(UUID userId);
	
}