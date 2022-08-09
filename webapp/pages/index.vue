<template>
  <v-row justify="center" align="center">
    <v-col cols="12" sm="8" md="6">
      <v-card>
        <v-card-text>
          <pre>loggedIn = {{ loggedIn }}</pre>
          <pre>user = {{ user }}</pre>
        </v-card-text>
        <v-card-actions>
          <v-btn v-if="loggedIn" to="/auth/logout" block color="primary">Logout</v-btn>
          <v-btn v-else to="/auth/login" block color="primary">Login</v-btn>
        </v-card-actions>
      </v-card>
    </v-col>
  </v-row>
</template>

<script lang="ts">
import { defineComponent, useContext, useRouter } from '@nuxtjs/composition-api'

export default defineComponent({
  setup() {
    const { loggedIn, user } = useContext().$auth

    return { loggedIn, user }
  },
})
</script>
