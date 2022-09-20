package com.crunchdao.app.service.submission.api.round;

import java.util.UUID;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RoundDto {
	
	private UUID id;
	private StateDescription state;
	
	@Data
	@Accessors(chain = true)
	public static class StateDescription {
		
		private boolean canSubmit;
		
	}
	
}