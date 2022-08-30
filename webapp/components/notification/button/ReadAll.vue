<template>
  <v-btn
    outlined
    text
    color="warning"
    small
    :loading="loading"
    @click="onClick"
  >
    <v-icon left>mdi-close</v-icon>
    Read All
  </v-btn>
</template>

<script lang="ts">
import { defineComponent, useContext } from '@nuxtjs/composition-api'
import { createPendingAction } from '@/composables/action'

export default defineComponent({
  emits: {
    readAll: () => true,
  },
  setup(_, { emit }) {
    const { $axios, $dialog } = useContext()

    const { loading, execute: onClick } = createPendingAction(async () => {
      const response = await $dialog.confirm({
        type: 'warning',
        title: 'Marking all as read',
        text: 'Are you sure you want to mark all of your notification as read?',
      })

      if (response) {
        await $axios.$delete(`/v1/notifications`)

        $dialog.notify.success('All notification marked as read!')
        emit('readAll')
      }
    })

    return {
      loading,
      onClick,
    }
  },
})
</script>
