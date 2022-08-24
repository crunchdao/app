<template>
  <div>
    <settings-title title="Notifications" />
  </div>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  layout: 'settings',
  head: {
    title: 'Notifications Settings',
  },
})
</script>
