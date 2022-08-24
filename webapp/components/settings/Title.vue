<template>
  <div>
    <v-card-title>
      {{ title }}
      <v-spacer />
      <slot>
        <v-btn style="visibility: hidden" />
      </slot>
    </v-card-title>
    <v-divider />
  </div>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  props: {
    title: {
      type: String,
      required: true,
    },
  },
  setup(props) {
    return {
      title: props.title,
    }
  },
})
</script>
