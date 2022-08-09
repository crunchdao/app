<template>
  <v-list-item @click="prompt">
    <v-list-item-avatar>
      <v-icon color="error">mdi-delete</v-icon>
    </v-list-item-avatar>
    <v-list-item-content>
      <v-list-item-title class="error--text"> Delete </v-list-item-title>
      <v-list-item-subtitle class="error--text">
        Make sure automation(s) will not break
      </v-list-item-subtitle>
    </v-list-item-content>
  </v-list-item>
</template>

<script lang="ts">
import { defineComponent, useContext } from '@nuxtjs/composition-api'
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

    const prompt = async () => {
      const response = await $dialog.confirm({
        type: 'error',
        title: 'Deletion confirmation',
        text: `Are you sure you want to delete this API Key?`,
      })

      if (response) {
        try {
          await $axios.delete(`/v1/api-keys/${apiKey!.id}`)

          $dialog.notify.success(`API Key deleted!`)
          emit('delete')
        } catch (error: Error | any) {
          const message =
            error?.response?.data?.message || error?.message || String(error)
          $dialog.notify.error(`Could not delete: ${message}`)
        }
      }
    }

    return { apiKey, prompt }
  },
})
</script>
