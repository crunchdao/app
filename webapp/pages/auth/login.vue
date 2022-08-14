<template>
  <p>loading</p>
</template>

<script lang="ts">
import { defineComponent, useContext, useRoute } from '@nuxtjs/composition-api'

export default defineComponent({
  layout: 'empty',
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
