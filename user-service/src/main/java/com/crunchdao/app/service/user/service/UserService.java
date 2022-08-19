package com.crunchdao.app.service.user.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.user.dto.UserDto;
import com.crunchdao.app.service.user.dto.UserWithIdDto;
import com.crunchdao.app.service.user.entity.User;
import com.crunchdao.app.service.user.exception.DuplicateUsernameException;
import com.crunchdao.app.service.user.exception.UserNotFoundException;
import com.crunchdao.app.service.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository repository;
	private final RabbitMQSender rabbitMQSender;
	
	public Optional<UserDto> findById(UUID id) {
		return repository.findById(id).map(User::toDto);
	}
	
	public PageResponse<UserDto> list(Pageable pageable) {
		Page<User> apiKeys = repository.findAll(pageable);
		
		return new PageResponse<>(apiKeys, (user) -> user.toDto().stripSensitive());
	}
	
	public UserDto create(UserWithIdDto body) {
		try {
			UserDto dto = repository.save(new User()
				.setId(body.getId())
				.setFirstName(body.getFirstName())
				.setLastName(body.getLastName())
				.setUsername(body.getUsername())
				.setEmail(body.getEmail())
				.setCreatedAt(LocalDateTime.now())
				.setUpdatedAt(LocalDateTime.now())).toDto();
			
			rabbitMQSender.sendCreated(dto);
			
			return dto;
		} catch (DuplicateKeyException exception) {
			log.warn("Could not create user", exception);
			
			throw new DuplicateUsernameException(body.getUsername());
		}
	}
	
	@Transactional
	public void delete(UUID id) {
		if (repository.removeById(id) == 0) {
			throw new UserNotFoundException(id);
		}
		
		rabbitMQSender.sendDeleted(id);
	}
	
}