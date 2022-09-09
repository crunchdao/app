<template>
  <v-card class="red--text">
    <card-title> Danger Zone </card-title>
    <v-list>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title> Delete your account </v-list-item-title>
          <v-list-item-subtitle>
            Once you delete an account, there is no going back. Please be
            certain.
          </v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn outlined color="red" to="/account/settings/delete">
            Delete your account
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </v-list>
  </v-card>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  setup() {
    return {}
  },
})
</script>
