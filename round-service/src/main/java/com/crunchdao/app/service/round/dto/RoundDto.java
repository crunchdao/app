package com.crunchdao.app.service.round.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.crunchdao.app.service.round.entity.Round;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(name = "Round")
public class RoundDto {
	
	private UUID id;
	private long number;
	private LocalDateTime start;
	private LocalDateTime end;
	private StateDescription state;
	
	@Data
	@Accessors(chain = true)
	public static class StateDescription {
		
		private String name;
		private boolean canSubmit;
		private boolean canSelect;
		
		public static StateDescription from(Round.State state) {
			return new StateDescription()
				.setName(state.name())
				.setCanSelect(state.isCanSelect())
				.setCanSubmit(state.isCanSubmit());
		}
		
	}
	
}