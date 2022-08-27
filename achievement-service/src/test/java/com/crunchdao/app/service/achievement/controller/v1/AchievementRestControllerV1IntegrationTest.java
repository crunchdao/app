package com.crunchdao.app.service.achievement.controller.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
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
import com.crunchdao.app.service.achievement.data.AchievementCategories;
import com.crunchdao.app.service.achievement.data.Achievements;
import com.crunchdao.app.service.achievement.entity.Achievement;
import com.crunchdao.app.service.achievement.entity.AchievementCategory;
import com.crunchdao.app.service.achievement.exception.AchievementNotFoundException;
import com.crunchdao.app.service.achievement.repository.AchievementCategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AchievementServiceApplication.class)
@AutoConfigureMockMvc
@DirtiesContext
class AchievementRestControllerV1IntegrationTest extends BaseIntegrationTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	AchievementCategoryRepository repository;
	
	@Nested
	class ListEndpoint {
		
		@Test
		void happy() throws Exception {
			mockMvc
				.perform(get(AchievementRestControllerV1.BASE_ENDPOINT))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(Achievements.values())));
		}
		
		@Test
		void category() throws Exception {
			mockMvc
				.perform(get(AchievementRestControllerV1.BASE_ENDPOINT)
					.param(AchievementRestControllerV1.CATEGORY_PARAMETER, UUID.randomUUID().toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());
		}
		
		@Test
		void category_notFound() throws Exception {
			final AchievementCategory category = AchievementCategories.TOURNAMENT;
			final List<Achievement> achievements = Achievements.values(category);
			
			mockMvc
				.perform(get(AchievementRestControllerV1.BASE_ENDPOINT)
					.param(AchievementRestControllerV1.CATEGORY_PARAMETER, category.getId().toString()))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(achievements)));
		}
		
	}
	
	@Nested
	class ShowEndpoint {
		
		@Test
		void happy() throws Exception {
			final Achievement achievement = Achievements.A_NEW_CRUNCHER_IS_BORN;
			
			mockMvc
				.perform(get(AchievementRestControllerV1.ID_ENDPOINT, achievement.getId()))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(achievement)));
		}
		
		@Test
		void notFound() throws Exception {
			mockMvc
				.perform(get(AchievementRestControllerV1.ID_ENDPOINT, UUID.randomUUID()))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AchievementNotFoundException.DEFAULT_MESSAGE));
		}
		
	}
	
}