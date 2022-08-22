export function extractMessage(error: Error | any, defaultError?: string): string {
  if (!error) {
    return defaultError || ""
  }

  return error?.response?.data?.message || error?.message || String(error) || defaultError || 'An error occured'
}
