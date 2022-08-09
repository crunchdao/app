
import {
  computed,
  ComputedGetter,
  DebuggerOptions,
} from '@nuxtjs/composition-api'

// The bug from https://github.com/nuxt-community/composition-api/issues/19 is still persisting for some reason...
export function fixedComputed<T>(getter: ComputedGetter<T>, debugOptions?: DebuggerOptions) {
  const storage = computed<T>({
    get() {
      return getter()
    },
    set(newValue) {
      if (storage.effect) {
        // should be a Vue's Watcher
        storage.effect.value = newValue
      }
    }
  }, debugOptions)

  return storage
}