<template>
  <div />
</template>

<script lang="ts">
import { defineComponent, useContext, useRouter } from '@nuxtjs/composition-api'

export default defineComponent({
  layout: 'loading',
  head: {
    title: 'Keycloak Callback',
  },
  setup() {
    const { $auth } = useContext()

    if (!$auth.loggedIn) {
      useRouter().replace('/')
    }
  },
})
</script>
