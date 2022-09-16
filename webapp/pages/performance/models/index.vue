<template>
  <v-card>
    <card-title> Models </card-title>
    <v-card-actions>
      <v-btn to="/performance/models/create"> create </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  head: {
    title: 'Models',
  },
})
</script>
