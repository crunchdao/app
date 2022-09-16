package com.crunchdao.app.service.model.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.crunchdao.app.service.model.configuration.ModelConfigurationProperties;
import com.crunchdao.app.service.model.dto.ModelCreateForm;
import com.crunchdao.app.service.model.dto.ModelUpdateForm;
import com.crunchdao.app.service.model.entity.Model;
import com.crunchdao.app.service.model.exception.NameAlreadyTakenException;
import com.crunchdao.app.service.model.exception.TooMuchModelException;
import com.crunchdao.app.service.model.repository.ModelRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ModelService {
	
	private final ModelRepository repository;
	private final ModelConfigurationProperties properties;
	
	public Optional<Model> findById(UUID id) {
		return repository.findById(id);
	}
	
	public List<Model> findAllByUserId(UUID userId) {
		return repository.findAllByUserId(userId);
	}
	
	public Model create(ModelCreateForm body, UUID userId) {
		if (!isUnderTheLimit(userId)) {
			throw new TooMuchModelException(properties.getLimit());
		}
		
		var model = save(new Model()
			.setUserId(userId)
			.setName(body.getName())
			.setComment(body.getComment()));
		
		// TODO Emit event
		
		return model;
	}
	
	public Model update(Model model, ModelUpdateForm body) {
		if (body.getName() != null) {
			model.setName(body.getName());
		}
		
		if (body.getComment() != null) {
			model.setComment(body.getComment());
		}
		
		model = save(model);
		
		// TODO Handle name collision
		// TODO Emit event
		
		return model;
	}
	
	public boolean isUnderTheLimit(UUID userId) {
		Long limit = properties.getLimit();
		if (limit == null) {
			return true;
		}
		
		long count = repository.countByUserId(userId);
		return count < limit;
	}
	
	private Model save(Model model) {
		try {
			return repository.saveAndFlush(model);
		} catch (DataIntegrityViolationException exception) {
			throw new NameAlreadyTakenException(model.getName(), exception);
		}
	}
	
}