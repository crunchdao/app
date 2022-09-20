import {
  reactive,
  Ref,
  ref,
  useContext
} from '@nuxtjs/composition-api'
import { AxiosRequestConfig } from 'axios'
import cloneDeep from 'lodash.clonedeep';
import { ApiError } from '~/models'

export interface Options<T> extends AxiosRequestConfig {
  inputs: { [key: string]: any }
  defaultValue?: T
  onSuccess?: (result: Ref<T>) => Promise<void> | void
  onFailure?: (error: Error | any) => Promise<void> | void
  onFinally?: (result: Ref<T | null>) => Promise<void> | void
  throw?: boolean
  formData?: boolean
}

export function createPendingRequest<T extends { [key: string]: any }>(
  options: Options<T>
) {
  const { $axios } = useContext()

  const loading = ref(false)
  const value = ref(options.defaultValue ?? null) as Ref<T | null>

  const inputs = reactive(cloneDeep(options.inputs) as T)

  const keys = Object.keys(options.inputs)

  const errorMessage = ref() as Ref<string | null>
  const validations = reactive(
    keys.reduce((accumulator, key: string) => {
      accumulator[key] = []
      return accumulator
    }, {} as { [key: string]: Array<string> })
  )

  const resetInputs = () => {
    for (const key in options.inputs) {
      inputs[key as keyof T] = cloneDeep(options.inputs[key])
    }
  }

  const resetValue = () => {
    value.value = options.defaultValue ?? null
  }

  const resetErrors = () => {
    errorMessage.value = null
    for (const key in validations) {
      if (keys.includes(key)) {
        validations[key] = [] as Array<string>
      }
    }
  }

  const resetAll = () => {
    resetInputs()
    resetValue()
    resetErrors()
  }

  const updateInputs = (object: Partial<T>) => {
    for (const key of Object.keys(inputs)) {
      if (key in object) {
        inputs[key as keyof T] = object[key]!
      }
    }
  }

  const submit = async (): Promise<Ref<T | null>> => {
    try {
      loading.value = true

      resetValue()
      resetErrors()

      let data: any = inputs
      if (options.formData === true) {
        const formData = new FormData()

        for (const [key, value] of Object.entries(inputs)) {
          formData.append(key, value)
        }

        data = formData
      }

      value.value = await $axios.$request({
        data,
        ...options,
      })

      await options.onSuccess?.(value as Ref<T>)
    } catch (error: Error | any) {
      const response = error?.response
      if (response) {
        const { data } = response as { data: ApiError }

        if (data?.code == "VALIDATION_FAILED") {
          for (const { property, message } of data.fieldErrors || []) {
            validations[property].push(message)
          }
        } else {
          errorMessage.value = data?.message || 'An error occured'
        }
      } else {
        errorMessage.value =
          error?.message || String(error) || 'An error occured'
      }

      await options.onFailure?.(error)

      if (options.throw) {
        throw error
      }
    } finally {
      loading.value = false

      await options.onFinally?.(value)
    }

    return value
  }

  return {
    loading,
    inputs,
    value,
    errorMessage,
    validations,
    resetInputs,
    resetValue,
    resetErrors,
    resetAll,
    updateInputs,
    submit,
  }
}
