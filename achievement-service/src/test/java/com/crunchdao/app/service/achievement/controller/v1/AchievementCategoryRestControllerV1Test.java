package com.crunchdao.app.service.achievement.controller.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import com.crunchdao.app.service.achievement.BaseMongoTest;
import com.crunchdao.app.service.achievement.data.AchievementCategories;
import com.crunchdao.app.service.achievement.entity.AchievementCategory;
import com.crunchdao.app.service.achievement.exception.AchievementCategoryNotFoundException;
import com.crunchdao.app.service.achievement.repository.AchievementCategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AchievementServiceApplication.class)
@AutoConfigureMockMvc
@DirtiesContext
class AchievementCategoryRestControllerV1Test extends BaseMongoTest {
	
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
				.perform(get(AchievementCategoryRestControllerV1.BASE_ENDPOINT))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(AchievementCategories.values())));
		}
		
	}
	
	@Nested
	class ShowEndpoint {
		
		@Test
		void happy() throws Exception {
			final AchievementCategory category = AchievementCategories.TOURNAMENT;
			
			mockMvc
				.perform(get(AchievementCategoryRestControllerV1.ID_ENDPOINT, category.getId()))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(category)));
		}
		
		@Test
		void notFound() throws Exception {
			mockMvc
				.perform(get(AchievementCategoryRestControllerV1.ID_ENDPOINT, UUID.randomUUID()))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(AchievementCategoryNotFoundException.DEFAULT_MESSAGE));
		}
		
	}
	
}