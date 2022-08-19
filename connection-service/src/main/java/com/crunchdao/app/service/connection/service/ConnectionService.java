package com.crunchdao.app.service.connection.service;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.connection.dto.ConnectionDto;
import com.crunchdao.app.service.connection.entity.Connection;
import com.crunchdao.app.service.connection.exception.ConnectionNotFoundException;
import com.crunchdao.app.service.connection.handler.ConnectionIdentity;
import com.crunchdao.app.service.connection.repository.ConnectionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ConnectionService {
	
	private final ConnectionRepository repository;
	private final RabbitMQSender rabbitMQSender;
	
	public PageResponse<ConnectionDto> listForUserId(UUID userId, Pageable pageable) {
		return new PageResponse<>(repository.findAllByUserId(userId, pageable), Connection::toDto);
	}
	
	public PageResponse<ConnectionDto> listPublicForUserId(UUID userId, Pageable pageable) {
		return new PageResponse<>(repository.findAllByUserIdAndIsPublicTrue(userId, pageable), Connection::toDto);
	}
	
	public ConnectionDto connect(UUID userId, String type, ConnectionIdentity identity) {
		ConnectionDto connection = repository.save(repository.findByUserIdAndType(userId, type)
			.orElseGet(() -> new Connection()
				.setUserId(userId)
				.setType(type))
			.mergeIdentity(identity)).toDto();
		
		rabbitMQSender.sendCreated(connection);
		
		return connection;
	}
	
	@Transactional
	public void disconnect(UUID userId, String type) {
		if (repository.deleteByUserIdAndType(userId, type) == 0) {
			throw new ConnectionNotFoundException(userId, type);
		}
		
		rabbitMQSender.sendDeleted(userId, type);
	}
	
	@Transactional
	public void deleteAllByUser(UUID userId) {
		repository.deleteAllByUserId(userId);
	}
	
}