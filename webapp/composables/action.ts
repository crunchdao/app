import {
  Ref,
  ref,
  useContext
} from '@nuxtjs/composition-api'
import defu from "defu"
import { extractMessage } from '~/utilities/error'

export interface Options {
  notifyError?: boolean
}

export function createPendingAction(
  callback: () => Promise<void>,
  options?: Options
) {
  const { $dialog } = useContext()

  options = defu(options || {}, {
    notifyError: true
  })

  const loading = ref(false)
  const execute = async () => {
    try {
      loading.value = true

      await callback()

      return true
    } catch (error: Error | any) {
      console.log(error)
      if (options!.notifyError) {
        $dialog.notify.error(extractMessage(error))
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
