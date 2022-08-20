<template>
  <v-tooltip top>
    <template #activator="{ on, attrs }">
      <v-btn
        icon
        class="mx-2"
        :loading="loading"
        v-bind="attrs"
        @click="onConnect"
        v-on="on"
      >
        <v-img
          :src="icon"
          max-height="32"
          max-width="32"
        />
      </v-btn>
    </template>
    <span>{{ type }}</span>
  </v-tooltip>
</template>

<script lang="ts">
import {
  defineComponent,
  useContext,
} from '@nuxtjs/composition-api'
import { createPendingAction } from '@/composables/action'
import { fixedComputed } from '~/composables/hack'

export default defineComponent({
  props: {
    type: {
      type: String,
      required: true,
    },
  },
  setup(props) {
    const { $axios } = useContext()
    const { type } = props

    const { loading, execute: onConnect } = createPendingAction(async () => {
      const { url } = await $axios.$post(`/v1/connections/${type}`)
      window.location = url
    })

    const icon = fixedComputed(() =>
      require(`@/assets/connection/${type!.toLowerCase()}.svg`)
    )

    return {
      type,
      loading,
      onConnect,
      icon,
    }
  },
})
</script>
