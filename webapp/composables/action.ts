import {
  Ref,
  ref,
  useContext
} from '@nuxtjs/composition-api'
import defu from "defu"
import { extractMessage } from '~/utilities/error'

export interface Options {
  notifyError?: boolean,
  errorMessagePhrase?: string
}

export function createPendingAction<Args extends any[]>(
  callback: (...args: [...Args]) => void | Promise<void>,
  options?: Options
) {
  const { $dialog } = useContext()

  options = defu(options || {}, {
    notifyError: true
  })

  const loading = ref(false)
  const execute = async (...args: [...Args]) => {
    try {
      loading.value = true

      await callback(...args)

      return true
    } catch (error: Error | any) {
      console.log(error)
      if (options!.notifyError) {
        let message = extractMessage(error)

        const { errorMessagePhrase } = options!
        if (errorMessagePhrase) {
          message = `${errorMessagePhrase}: ${message}`
        }
        
        $dialog.notify.error(message)
      }

      return false
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    execute
  }
}
