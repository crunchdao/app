<template>
  <div>
    <nuxt-child />
  </div>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

const links = [
  {
    title: 'Settings',
    to: '/account/settings',
  },
  {
    title: 'Levels',
    to: '/account/levels',
  },
  {
    title: 'Notifications',
    to: '/account/notifications',
  },
  {
    title: 'Referrals',
    to: '/account/referrals',
  },
  {
    title: 'API Keys',
    to: '/account/api-keys',
  },
]

export default defineComponent({
  head: {
    title: 'Admin',
    titleTemplate: '%s - Admin',
  },
  layout: "admin",
  setup() {
    return {
      links,
    }
  },
})
</script>
