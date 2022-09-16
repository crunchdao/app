package com.crunchdao.app.service.model.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crunchdao.app.service.model.entity.Model;

public interface ModelRepository extends JpaRepository<Model, UUID> {
	
	List<Model> findAllByUserId(UUID userId);

	long countByUserId(UUID userId);
	
}