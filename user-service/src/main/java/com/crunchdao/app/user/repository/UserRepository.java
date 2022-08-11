package com.crunchdao.app.user.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.crunchdao.app.user.entity.User;

public interface UserRepository extends MongoRepository<User, UUID> {
	
}