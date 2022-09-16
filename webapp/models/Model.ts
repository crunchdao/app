import { Entity } from "./Entity";
import { LocalDateTime, UUID } from "./types";

export interface Model extends Entity<UUID> {
  userId: UUID;
  name: string;
  comment?: string;
  createdAt: LocalDateTime;
  updatedAt: LocalDateTime;
}