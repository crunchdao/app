import { Entity } from "./Entity"
import { LocalDateTime, UUID } from "./types"

export interface ApiKey extends Entity<UUID> {
  userId: UUID
  name: string
  description: string
  plain?: string
  truncated: string
  createdAt: LocalDateTime
  updatedAt: LocalDateTime
  scopes: Array<string>
}