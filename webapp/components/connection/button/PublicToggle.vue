<template>
  <v-btn :loading="loading" text outlined @click.prevent="onClick">
    <v-icon left>{{ textAndIcon.icon }}</v-icon>
    {{ textAndIcon.text }}
  </v-btn>
</template>

<script lang="ts">
import { defineComponent, PropType, useContext } from '@nuxtjs/composition-api'
import { createPendingAction } from '@/composables/action'
import { Connection } from '~/models'
import { fixedComputed } from '~/composables/hack'

const textAndIcons = {
  true: {
    text: 'Hide off profile',
    icon: 'mdi-eye-off',
    success: 'Now visible on your profile.',
  },
  false: {
    text: 'Show on profile',
    icon: 'mdi-eye',
    success: 'Now hidden from your profile.',
  },
} as { [key: string]: { text: string; icon: string; success: string } }

export default defineComponent({
  props: {
    connection: {
      type: Object as PropType<Connection>,
      required: true,
    },
  },
  emits: {
    update: (_connection: Connection) => true,
  },
  setup(props, { emit }) {
    const { $axios, $dialog } = useContext()
    const { connection } = props

    const textAndIcon = fixedComputed(
      () => textAndIcons[String(connection.public)]
    )

    const { loading, execute: onClick } = createPendingAction(async () => {
      const updated = await $axios.$patch(
        `/v1/connections/${connection.type}`,
        {
          public: !connection.public,
        }
      )

      emit('update', updated)
      $dialog.notify.success(textAndIcon.value.success)
    })

    return {
      loading,
      onClick,
      textAndIcon,
    }
  },
})
</script>
