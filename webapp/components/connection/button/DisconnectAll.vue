<template>
  <v-btn :loading="loading" color="error" text outlined @click="onClick">
    <v-icon left>mdi-close</v-icon>
    Disconnect All
  </v-btn>
</template>

<script lang="ts">
import { defineComponent, useContext } from '@nuxtjs/composition-api'
import { createPendingAction } from '@/composables/action'

export default defineComponent({
  emits: {
    disconnect: () => true,
  },
  setup(_, { emit }) {
    const { $axios, $dialog } = useContext()

    const { loading, execute: onClick } = createPendingAction(async () => {
      const response = await $dialog.confirm({
        type: 'error',
        title: 'Disconnect All confirmation',
        text: 'Are you sure you want to disconnect all connections?',
      })

      if (response) {
        await $axios.$delete(`/v1/connections`)

        emit('disconnect')
      }
    })

    return {
      loading,
      onClick,
    }
  },
})
</script>
