<template>
  <p>loading</p>
</template>

<script lang="ts">
import { defineComponent, useContext } from '@nuxtjs/composition-api'

export default defineComponent({
  setup() {
    const { $auth } = useContext()

    $auth.loginWith('keycloak')
  },
})
</script>
