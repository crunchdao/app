<template>
  <v-btn :icon="icon" :color="color" to="/auth/logout">
    <v-icon v-if="icon">mdi-logout</v-icon>
    <template v-else>
      Log out
    </template>
  </v-btn>
</template>

<script lang="ts">
import { computed, defineComponent, toRefs } from '@nuxtjs/composition-api'

export default defineComponent({
  props: {
    icon: Boolean,
  },
  setup(props) {
    const { icon } = toRefs(props)
    const color = computed<string | undefined>(() => !icon.value ? 'primary' : undefined)

    return {
      icon,
      color
    }
  },
})
</script>
