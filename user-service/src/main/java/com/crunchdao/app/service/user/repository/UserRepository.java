package com.crunchdao.app.service.user.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.crunchdao.app.service.user.entity.User;

public interface UserRepository extends MongoRepository<User, UUID> {
	
	long removeById(UUID id);

	Page<User> findAllByUsernameLike(String username, Pageable pageable);
	
}