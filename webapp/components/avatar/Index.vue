<template>
  <v-avatar :size="props.size">
    <v-img :src="url" />
    <slot name="after" />
  </v-avatar>
</template>

<script lang="ts">
import { defineComponent, PropType } from '@nuxtjs/composition-api'
import { fixedComputed } from '~/composables/hack'
import { UUID } from '~/models'

export default defineComponent({
  props: {
    userId: {
      type: String as PropType<UUID>,
      required: true,
    },
    version: {
      type: Number,
      default: 0,
    },
    size: [Number, String],
  },
  setup(props) {
    const url = fixedComputed(
      () =>
        `https://datacrunch-com-public.s3.amazonaws.com/app/avatar/users/${props.userId}.png?version=${props.version}`
    )

    return { url, props }
  },
})
</script>
