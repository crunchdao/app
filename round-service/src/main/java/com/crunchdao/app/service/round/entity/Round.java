package com.crunchdao.app.service.round.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.crunchdao.app.service.round.dto.RoundDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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
	private LocalDateTime start;
	
	@Field
	private LocalDateTime end;
	
	@Field
	private State state;
	
	public RoundDto toDto() {
		return new RoundDto()
			.setId(id)
			.setNumber(number)
			.setStart(start)
			.setEnd(end)
			.setState(RoundDto.StateDescription.from(state));
	}
	
	@AllArgsConstructor
	@Getter
	public enum State {
		
		CREATED,
		STARTED(true, true),
		CLOSING(false, true),
		CLOSED;
		
		private final boolean canSubmit;
		private final boolean canSelect;
		
		private State() {
			this(false, false);
		}
		
	}
	
}