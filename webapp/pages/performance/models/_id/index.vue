<template>
  <v-row>
    <v-col cols="12">
      <v-card>
        <card-title> {{ model.name }} </card-title>
      </v-card>
    </v-col>
    <v-col cols="12">
      <v-card>
        <card-title> My Meta-Model Contribution </card-title>
        <v-card-text>
          <placeholder-chart height="400" />
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
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
