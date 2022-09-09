<template>
  <v-flex class="d-flex flex-column">
    <span class="subtitle-2 text--secondary text-uppercase"> Token Price </span>
    <span class="h6">$4.00</span>
  </v-flex>
</template>

<script lang="ts">
import { computed, defineComponent, ref } from '@nuxtjs/composition-api'

export default defineComponent({
  setup() {
    // TODO: Fetch from the API
    const price = ref(4.0)

    const formatted = computed(() => {
      const decimal = price.value.toLocaleString('en-US', {
        minimumIntegerDigits: 2,
        useGrouping: false,
      })

      return `$${decimal}`
    })

    return {
      formatted,
    }
  },
})
</script>

