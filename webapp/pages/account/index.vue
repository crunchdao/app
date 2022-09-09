<template>
  <div />
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  layout: 'loading',
  head: {
    title: 'Account',
  },
  mounted() {
    this.$router.replace({
      path: "/account/settings"
    })
  }
})
</script>
