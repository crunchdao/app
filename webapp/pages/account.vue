<template>
  <v-container>
      <v-toolbar rounded class="mb-6">
        <v-tabs>
          <v-tab v-for="link in links" :key="link.to" :to="link.to">
            {{ link.title }}
          </v-tab>
        </v-tabs>
      </v-toolbar>
    <nuxt-child />
  </v-container>
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
    title: 'Account',
    titleTemplate: '%s - Account',
  },
  setup() {
    return {
      links,
    }
  },
})
</script>
