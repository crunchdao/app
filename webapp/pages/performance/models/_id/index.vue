<template>
  <v-row>
    <v-col cols="12">
      <v-card>
        <card-title>
          {{ model.name }}
          <template #action>
            <v-btn text outlined :to="toEdit">
              <v-icon left>mdi-pencil</v-icon>
              edit
            </v-btn>
          </template>
        </card-title>
        <v-card-subtitle>
          Comment:
          <pre
            v-if="model.comment"
          ><code class="d-block">{{ model.comment }}</code></pre>
          <pre v-else><code><small>(no comment)</small></code></pre>
        </v-card-subtitle>
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
import {
  defineComponent,
  PropType,
  toRefs,
  useMeta,
} from '@nuxtjs/composition-api'
import { fixedComputed } from '~/composables/hack'
import { Model } from '~/models'

export default defineComponent({
  head: {
    title: 'Models',
  },
  props: {
    model: {
      type: Object as PropType<Model>,
      required: true,
    },
  },
  setup(props) {
    const { model } = toRefs(props)

    const toEdit = fixedComputed(
      () => `/performance/models/${model.value.id}/edit`
    )

    useMeta(() => ({
      title: model.value.name,
    }))

    return {
      toEdit,
      model,
    }
  },
})
</script>
