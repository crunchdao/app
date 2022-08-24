<template>
  <v-btn color="error" text outlined :loading="loading" @click="prompt">
    <v-icon left>mdi-delete</v-icon>
    Revoke All
  </v-btn>
</template>

<script lang="ts">
import { defineComponent, ref, useContext } from '@nuxtjs/composition-api'

export default defineComponent({
  emits: {
    delete: () => true,
  },
  setup(_, { emit }) {
    const { $axios, $dialog } = useContext()

    const loading = ref(false)
    const prompt = async () => {
      const response = await $dialog.confirm({
        type: 'error',
        title: 'Deletion confirmation',
        text: `Are you sure you want to delete ALL API Keys?`,
      })

      if (response) {
        try {
          loading.value = true

          await $axios.delete(`/v1/api-keys`)

          $dialog.notify.success(`All API Keys revoked!`)
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

    return { prompt, loading }
  },
})
</script>
