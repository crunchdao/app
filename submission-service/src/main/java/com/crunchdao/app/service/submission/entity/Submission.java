package com.crunchdao.app.service.submission.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.crunchdao.app.service.submission.dto.SubmissionDto;

import lombok.Data;
import lombok.experimental.Accessors;

@Document(collection = "submissions")
@Data
@Accessors(chain = true)
public class Submission {
	
	@Id
	private UUID id;
	
	@Field
	private UUID modelId;
	
	@Field
	private UUID userId;
	
	@Field
	private UUID roundId;
	
	@Field
	private String comment;
	
	@Field
	private boolean selected;
	
	@Field
	private SelectedBy selectedBy;
	
	@Field
	private Status status;
	
	@Field
	private State state;
	
	@Field
	private LocalDateTime createdAt;
	
	@Field
	private LocalDateTime updatedAt;
	
	@Field
	private Double score;
	
	@Field
	private FileMetadata fileMetadata;
	
	public SubmissionDto toDto() {
		return new SubmissionDto()
			.setId(id)
			.setModelId(modelId)
			.setUserId(userId)
			.setRoundId(roundId)
			.setComment(comment)
			.setSelected(selected)
			.setSelectedBy(selectedBy)
			.setStatus(status)
			.setCreatedAt(createdAt)
			.setUpdatedAt(updatedAt)
			.setFileMetadata(fileMetadata.toDto());
	}
	
	public enum Status {
		
		PENDING, SUCCESS, FAILURE;
	
	}
	
	public enum State {
		
		PENDING, COMPUTING, COMPUTED;
	
	}
	
	public enum SelectedBy {
		
		USER, SYSTEM;
	
	}
	
	@Data
	@Accessors(chain = true)
	public static class FileMetadata {
		
		@Field
		private String name;
		
		@Field
		private long length;
		
		@Field
		private String hash;
		
		@Field
		@Indexed(unique = true)
		private String url;
		
		public SubmissionDto.FileMetadataDto toDto() {
			return new SubmissionDto.FileMetadataDto()
				.setName(name)
				.setLength(length)
				.setHash(hash);
		}
		
	}
	
}