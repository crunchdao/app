<template>
  <div />
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  layout: 'loading',
  head: {
    title: 'Settings',
  },
  mounted() {
    this.$router.replace({
      path: "/settings/profile"
    })
  }
})
</script>
