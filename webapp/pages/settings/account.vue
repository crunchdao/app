<template>
  <div>
    <v-card-title>
      Account
      <v-spacer />
      <v-btn style="visibility: hidden" />
    </v-card-title>
    <v-divider />
  </div>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  layout: 'settings',
})
</script>
