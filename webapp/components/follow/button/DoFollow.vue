<template>
  <v-btn :disabled="disabled" :loading="loading" @click="onClick">
    <v-icon left>mdi-heart</v-icon>
    follow
  </v-btn>
</template>

<script lang="ts">
import { defineComponent, toRefs, useContext } from '@nuxtjs/composition-api'
import { createPendingAction } from '~/composables/action'

/* naming the component "follow" cause problem */
export default defineComponent({
  props: {
    userId: {
      type: String,
      required: true,
    },
    disabled: Boolean,
  },
  emits: {
    follow: () => true,
  },
  setup(props, { emit }) {
    const { $axios } = useContext()
    const { userId, disabled } = toRefs(props)

    const { loading, execute: onClick } = createPendingAction(async () => {
      await $axios.$put(`/v1/follow/${userId.value}/followers`)

      emit('follow')
    })

    return {
      disabled,
      loading,
      onClick,
    }
  },
})
</script>
