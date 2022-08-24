<template>
  <div>
    <settings-account-delete />
  </div>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  layout: 'settings',
  head: {
    title: 'Account Settings',
  },
})
</script>
