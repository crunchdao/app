<template>
  <v-list-item @click="prompt">
    <v-list-item-avatar>
      <v-icon color="error">mdi-delete</v-icon>
    </v-list-item-avatar>
    <v-list-item-content>
      <v-list-item-title class="error--text"> Delete All </v-list-item-title>
      <v-list-item-subtitle class="error--text">
        All automation(s) might break
      </v-list-item-subtitle>
    </v-list-item-content>
  </v-list-item>
</template>

<script lang="ts">
import { defineComponent, useContext } from '@nuxtjs/composition-api'

export default defineComponent({
  emits: {
    delete: () => true,
  },
  setup(_, { emit }) {
    const { $axios, $dialog } = useContext()

    const prompt = async () => {
      const response = await $dialog.confirm({
        type: 'error',
        title: 'Deletion confirmation',
        text: `Are you sure you want to delete ALL API Keys?`,
      })

      if (response) {
        try {
          await $axios.delete(`/v1/api-keys`)

          $dialog.notify.success(`All API Keys deleted!`)
          emit('delete')
        } catch (error: Error | any) {
          const message =
            error?.response?.data?.message || error?.message || String(error)
          $dialog.notify.error(`Could not delete: ${message}`)
        }
      }
    }

    return { prompt }
  },
})
</script>
