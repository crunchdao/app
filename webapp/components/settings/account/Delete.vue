<template>
  <div>
    <v-card-title class="error--text">Delete account</v-card-title>
    <v-divider />
    <v-card-subtitle> This action cannot be undone. </v-card-subtitle>
    <v-card-actions>
      <v-btn outlined text color="error" to="/settings/account/delete">
        Delete your account
      </v-btn>
    </v-card-actions>
  </div>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  layout: 'settings',
  setup() {
    return {}
  },
})
</script>
