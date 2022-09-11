<template>
  <v-btn :disabled="disabled" :loading="loading" @click="onClick">
    <v-icon left>mdi-heart-broken</v-icon>
    unfollow
  </v-btn>
</template>

<script lang="ts">
import { defineComponent, toRefs, useContext } from '@nuxtjs/composition-api'
import { createPendingAction } from '~/composables/action'

export default defineComponent({
  props: {
    userId: {
      type: String,
      required: true,
    },
    disabled: Boolean,
  },
  emits: {
    unfollow: () => true,
  },
  setup(props, { emit }) {
    const { $axios } = useContext()
    const { userId, disabled } = toRefs(props)

    const { loading, execute: onClick } = createPendingAction(async () => {
      await $axios.$delete(`/v1/follow/${userId.value}/followers`)

      emit('unfollow')
    })

    return {
      disabled,
      loading,
      onClick,
    }
  },
})
</script>
