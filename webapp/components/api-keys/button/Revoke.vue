<template>
  <v-btn :loading="loading" color="error" text outlined @click.stop.prevent="prompt">
    <v-icon left>mdi-delete</v-icon>
    Revoke
  </v-btn>
</template>

<script lang="ts">
import { defineComponent, ref, useContext } from '@nuxtjs/composition-api'
import { ApiKey } from '@/models'

export default defineComponent({
  props: {
    apiKey: {
      required: true,
    } as Vue.PropOptions<ApiKey>,
  },
  emits: {
    delete: () => true,
  },
  setup(props, { emit }) {
    const { $axios, $dialog } = useContext()
    const { apiKey } = props

    const loading = ref(false)
    const prompt = async () => {
      const response = await $dialog.confirm({
        type: 'error',
        title: 'Deletion confirmation',
        text: `Are you sure you want to delete this API Key?`,
      })

      if (response) {
        try {
          loading.value = true

          await $axios.delete(`/v1/api-keys/${apiKey!.id}`)

          $dialog.notify.success(`API Key deleted!`)
          emit('delete')
        } catch (error: Error | any) {
          const message =
            error?.response?.data?.message || error?.message || String(error)
          $dialog.notify.error(`Could not delete: ${message}`)
        } finally {
          loading.value = false
        }
      }
    }

    return { apiKey, prompt, loading }
  },
})
</script>
