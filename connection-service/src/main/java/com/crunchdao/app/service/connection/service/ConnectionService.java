package com.crunchdao.app.service.connection.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.crunchdao.app.service.connection.dto.ConnectionDto;
import com.crunchdao.app.service.connection.dto.ConnectionUpdateForm;
import com.crunchdao.app.service.connection.entity.Connection;
import com.crunchdao.app.service.connection.exception.ConnectionNotFoundException;
import com.crunchdao.app.service.connection.exception.NoConnectionFoundException;
import com.crunchdao.app.service.connection.handler.ConnectionIdentity;
import com.crunchdao.app.service.connection.repository.ConnectionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ConnectionService {
	
	private final ConnectionRepository repository;
	private final RabbitMQSender rabbitMQSender;
	
	public Optional<ConnectionDto> findByUserIdAndType(UUID userId, String type) {
		return repository.findByUserIdAndType(userId, type).map(Connection::toDto);
	}
	
	public List<ConnectionDto> listForUserId(UUID userId) {
		return toDtos(repository.findAllByUserId(userId));
	}
	
	public List<ConnectionDto> listPublicForUserId(UUID userId) {
		return toDtos(repository.findAllByUserIdAndIsPublicTrue(userId));
	}
	
	public ConnectionDto update(UUID userId, String type, ConnectionUpdateForm body) {
		Connection connection = repository.findByUserIdAndType(userId, type).orElseThrow(() -> new ConnectionNotFoundException(userId, type));
		
		if (body.getIsPublic() != null) {
			connection.setPublic(body.getIsPublic());
		}
		
		return repository.save(connection).toDto();
	}
	
	public ConnectionDto connect(UUID userId, String type, ConnectionIdentity identity) {
		type = type.toUpperCase();
		
		final String finalType = type;
		ConnectionDto connection = repository.save(repository.findByUserIdAndType(userId, type)
			.orElseGet(() -> new Connection()
				.setUserId(userId)
				.setType(finalType))
			.mergeIdentity(identity)).toDto();
		
		rabbitMQSender.sendCreated(connection);
		
		return connection;
	}
	
	@Transactional
	public void disconnect(UUID userId, String type) {
		type = type.toUpperCase();
		
		if (repository.deleteByUserIdAndType(userId, type) == 0) {
			throw new ConnectionNotFoundException(userId, type);
		}
		
		rabbitMQSender.sendDeleted(userId, type);
	}
	
	@Transactional
	public void disconnectAll(UUID userId) {
		List<Connection> connections = repository.findAllByUserId(userId);
		
		if (repository.deleteAllByUserId(userId) == 0) {
			throw new NoConnectionFoundException(userId);
		}
		
		connections.forEach((connection) -> {
			rabbitMQSender.sendDeleted(userId, connection.getType());
		});
	}
	
	public static List<ConnectionDto> toDtos(List<Connection> connections) {
		return connections.stream().map(Connection::toDto).toList();
	}
	
}