<template>
  <div />
</template>

<script lang="ts">
import { defineComponent, useContext, useRouter } from '@nuxtjs/composition-api'

export default defineComponent({
  layout: 'loading',
  setup() {
    const { $auth } = useContext()

    if ($auth.loggedIn) {
      $auth.logout()
    } else {
      useRouter().replace('/')
    }
  },
})
</script>
