<template>
  <v-btn
    :loading="loading"
    color="error"
    text
    outlined
    @click.stop.prevent="onClick"
  >
    <v-icon left>mdi-close</v-icon>
    Disconnect
  </v-btn>
</template>

<script lang="ts">
import { defineComponent, PropType, useContext } from '@nuxtjs/composition-api'
import { createPendingAction } from '@/composables/action'
import { Connection } from '~/models'

export default defineComponent({
  props: {
    connection: {
      type: Object as PropType<Connection>,
      required: true,
    },
  },
  emits: {
    disconnect: () => true,
  },
  setup(props, { emit }) {
    const { $axios, $dialog } = useContext()
    const { connection } = props

    const { loading, execute: onClick } = createPendingAction(async () => {
      const response = await $dialog.confirm({
        type: 'error',
        title: 'Disconnect confirmation',
        text: 'Are you sure you want to disconnect this connection?',
      })

      if (response) {
        await $axios.$delete(`/v1/connections/${connection.type}`)

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
