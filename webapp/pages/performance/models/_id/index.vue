<template>
  <v-card>
    <card-title> {{ model.name }} </card-title>
  </v-card>
</template>

<script lang="ts">
import { defineComponent, PropType, toRefs } from '@nuxtjs/composition-api'
import { Model } from '~/models'

export default defineComponent({
  head: {},
  props: {
    model: {
      type: Object as PropType<Model>,
      required: true,
    },
  },
  setup(props) {
    const { model } = toRefs(props)

    return {
      model,
    }
  },
})
</script>
