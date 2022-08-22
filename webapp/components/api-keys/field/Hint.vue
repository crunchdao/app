<template>
  <v-hover v-model="hover">
    <v-text-field
      class="d-inline-block"
      dense
      hide-details
      readonly
      outlined
      style="font-family: monospace; width: 100px"
      label="hint"
      :value="text"
      @click.stop.prevent
    />
  </v-hover>
</template>

<script lang="ts">
import { defineComponent, ref } from '@nuxtjs/composition-api'
import { ApiKey } from '@/models'
import { fixedComputed } from '~/composables/hack'

export default defineComponent({
  props: {
    apiKey: {
      required: true,
    } as Vue.PropOptions<ApiKey>,
  },
  setup(props) {
    const { apiKey } = props

    const hover = ref(false)
    const text = fixedComputed(
      () => (hover.value ? apiKey!.truncated : '••••') + '••••'
    )

    return { apiKey, hover, text }
  },
})
</script>
