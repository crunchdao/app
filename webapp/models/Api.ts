export interface ApiError {
  code: string
  message: string
  fieldErrors?: Array<FieldError>
  [key: string]: any
}

export interface FieldError {
  code: string
  message: string
  property: string
  rejectedValue: any
  path: string
}

export interface PageResponse<T = any> {
  content: Array<T>
  pageNumber: number
  pageSize: number
  totalElements: number
  totalPages: number
  first: boolean
  last: boolean
}