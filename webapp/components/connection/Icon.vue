<template>
  <v-img :src="icon" max-height="32" max-width="32" />
</template>

<script lang="ts">
import { computed, defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  props: {
    type: {
      type: String,
      required: true,
    },
  },
  setup(props) {
    const icon = computed(() =>
      require(`@/assets/connection/${props.type.toLowerCase()}.svg`)
    )

    return {
      icon,
    }
  },
})
</script>
