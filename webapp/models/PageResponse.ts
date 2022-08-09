export interface PageResponse<T = any> {
  content: Array<T>
  pageNumber: number
  pageSize: number
  totalElements: number
  totalPages: number
  first: number
  last: number
}