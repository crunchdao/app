<template>
  <div />
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  layout: 'loading',
  head: {
    title: 'Performance',
  },
  mounted() {
    this.$router.replace({
      path: "/performance/profile"
    })
  }
})
</script>
