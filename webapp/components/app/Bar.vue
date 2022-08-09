<template>
  <v-app-bar app>
    <v-app-bar-title> Hello </v-app-bar-title>
    <v-spacer />
    <account-menu v-if="loggedIn" />
    <template v-else>
      <auth-button-register class="mr-2" />
      <auth-button-login />
    </template>
  </v-app-bar>
</template>

<script lang="ts">
import { defineComponent, useContext } from '@nuxtjs/composition-api'

export default defineComponent({
  setup() {
    const { $auth } = useContext()

    const { loggedIn } = $auth

    return { loggedIn }
  },
})
</script>
