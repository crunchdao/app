<template>
  <v-btn
    class="mx-2"
    text
    outlined
    :loading="loading"
    @click="onClick"
  >
    <v-icon left>mdi-connection</v-icon>
    connect
  </v-btn>
</template>

<script lang="ts">
import {
  defineComponent,
  PropType,
  toRefs,
  useContext,
} from '@nuxtjs/composition-api'
import { createPendingAction } from '@/composables/action'
import { ConnectionHandler } from '~/models'

export default defineComponent({
  props: {
    handler: {
      type: Object as PropType<ConnectionHandler>,
      required: true,
    },
  },
  setup(props) {
    const { $axios } = useContext()
    const { handler } = toRefs(props)

    const { loading, execute: onClick } = createPendingAction(async () => {
      const { url } = await $axios.$post(
        `/v1/connections/${handler.value.type}`
      )
      window.location = url
    })

    return {
      handler,
      loading,
      onClick,
    }
  },
})
</script>
