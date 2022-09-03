<template>
  <div>
    <v-hover>
      <template #default="{ hover }">
        <avatar :user-id="userId" :version="version" size="160">
          <template #after>
            <v-fade-transition>
              <v-overlay v-if="hover || loading" absolute>
                <v-btn icon :loading="loading" @click="onClick">
                  <v-icon>mdi-pencil</v-icon>
                </v-btn>
              </v-overlay>
            </v-fade-transition>
          </template>
        </avatar>
      </template>
    </v-hover>
    <input v-show="false" ref="input" type="file" @change="onChange" />
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  useContext,
} from '@nuxtjs/composition-api'
import { createPendingAction } from '~/composables/action'
import { fixedComputed } from '~/composables/hack'

export default defineComponent({
  setup() {
    const { $auth, $dialog, $axios } = useContext()
    const userId = fixedComputed(() => $auth.user?.id)

    const version = ref(0)

    const { loading, execute: upload } = createPendingAction(
      async (file: File) => {
        const formData = new FormData()
        formData.append('picture', file)

        await $axios.$post(`/v1/avatar`, formData)
        version.value = Date.now()
        $dialog.notify.success(`Avatar updated!`)
      }
    )

    const input = ref<HTMLInputElement>()
    const onChange = (event: Event) => {
      const file = input.value!.files?.[0]

      if (file) {
        upload(file)
      }

      input.value!.value = null as any
    }

    const onClick = () => {
      input.value!.click()
    }

    return { userId, version, onClick, input, onChange, loading }
  },
})
</script>
