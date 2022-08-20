<template>
  <p>loading</p>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  layout: 'settings',
  mounted() {
    this.$router.replace({
      path: "/settings/profile"
    })
  }
})
</script>
