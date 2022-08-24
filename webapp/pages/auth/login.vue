<template>
  <div />
</template>

<script lang="ts">
import { defineComponent, useContext, useRoute } from '@nuxtjs/composition-api'

export default defineComponent({
  layout: 'loading',
  setup() {
    const { $auth } = useContext()
    const route = useRoute()

    const username = route.value.query.username

    $auth.loginWith('keycloak', {
      params: {
        login_hint: username,
      },
    })
  },
})
</script>
