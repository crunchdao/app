package com.crunchdao.app.service.achievement.controller.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.crunchdao.app.service.achievement.AchievementServiceApplication;
import com.crunchdao.app.service.achievement.BaseIntegrationTest;
import com.crunchdao.app.service.achievement.data.Achievements;
import com.crunchdao.app.service.achievement.entity.Achievement;
import com.crunchdao.app.service.achievement.service.AchievementUserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AchievementServiceApplication.class)
@AutoConfigureMockMvc
@DirtiesContext
class AchievementUserRestControllerV1IntegrationTest extends BaseIntegrationTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	AchievementUserService service;
	
	@Nested
	class ListEndpoint {
		
		@Test
		void happy() throws Exception {
			final UUID userId = UUID.randomUUID();
			final Achievement achievement = Achievements.A_NEW_CRUNCHER_IS_BORN;
			
			mockMvc
				.perform(get(AchievementUserRestControllerV1.ID_ENDPOINT, userId.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());
			
			service.increment(achievement, userId, 1, null);
			
			mockMvc
				.perform(get(AchievementUserRestControllerV1.ID_ENDPOINT, userId.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].userId").value(userId.toString()))
				.andExpect(jsonPath("$[0].achievementId").value(achievement.getId().toString()))
				.andExpect(jsonPath("$[0].progress").value(1))
				.andExpect(jsonPath("$[0].unlocked").value(true))
				.andExpect(jsonPath("$[0].unlockedAt").isNotEmpty());
		}
		
	}
	
}