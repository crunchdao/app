package com.crunchdao.app.service.round.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.experimental.Accessors;

@Document(collection = "rounds")
@Data
@Accessors(chain = true)
public class Round {
	
	@Id
	private UUID id;
	
	@Field
	@Indexed(unique = true)
	private long number;
	
	@Field
	LocalDateTime start;
	
	@Field
	LocalDateTime end;
	
	@Field
	State state;
	
	public enum State {
		
		CREATED,
		STARTED,
		CLOSING,
		CLOSED;
	
	}
	
}