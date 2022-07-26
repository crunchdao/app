package com.crunchdao.app.service.round.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crunchdao.app.service.round.entity.Round;
import com.crunchdao.app.service.round.repository.RoundRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoundService {
	
	private final RoundRepository repository;
	
	public Page<Round> list(Pageable pageable) {
		return repository.findAll(pageable);
	}
	
	public Optional<Round> findActive() {
		return repository.findByStateIsIn(Arrays.asList(Round.State.STARTED, Round.State.CLOSING));
	}
	
	public Optional<Round> findById(UUID id) {
		return repository.findById(id);
	}
	
}