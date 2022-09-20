import { Entity } from "./Entity";
import { LocalDateTime, UUID } from "./types";

export interface SubmissionFileMetadata {
  name: string;
  length: number;
  hash: string;
}

export enum SubmissionSelectedBy {
  USER = "USER",
  SYSTEM = "SYSTEM"
}

export enum SubmissionStatus {
  PENDING = "PENDING",
  SUCCESS = "SUCCESS",
  FAILURE = "FAILURE",
}

export interface Submission extends Entity<UUID> {
  modelId: UUID;
  userId: UUID;
  roundId: UUID;
  comment: string;
  selected: boolean;
  selectedBy: SubmissionSelectedBy | null;
  status: string;
  createdAt: LocalDateTime;
  updatedAt: LocalDateTime;
  fileMetadata: SubmissionFileMetadata;
}
