import { Entity } from "./Entity"
import { LocalDateTime, UUID } from "./types"

export interface User extends Entity<UUID> {
  username: string
  firstName?: string
  lastName?: string
  email?: string
  createdAt: LocalDateTime
  updatedAt: LocalDateTime
}