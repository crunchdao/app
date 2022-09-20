package com.crunchdao.app.service.submission.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.crunchdao.app.service.submission.entity.Submission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(name = "Submission")
public class SubmissionDto {
	
	private UUID id;
	private UUID modelId;
	private UUID userId;
	private UUID roundId;
	private String comment;
	private boolean selected;
	private Submission.SelectedBy selectedBy;
	private Submission.Status status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private FileMetadataDto fileMetadata;
	
	@Data
	@Accessors(chain = true)
	@Schema(name = "FileMetadata")
	public static class FileMetadataDto {
		
		private String name;
		private long length;
		private String hash;
		
	}
	
}