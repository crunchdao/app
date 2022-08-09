import { UUID } from "./types";

export interface Entity<ID = UUID> {
  id: ID
}