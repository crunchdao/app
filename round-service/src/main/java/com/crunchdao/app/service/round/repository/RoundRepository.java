package com.crunchdao.app.service.round.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.crunchdao.app.service.round.entity.Round;
import com.crunchdao.app.service.round.entity.Round.State;

public interface RoundRepository extends MongoRepository<Round, UUID> {

	Optional<Round> findByStateIsIn(List<State> states);
	
}