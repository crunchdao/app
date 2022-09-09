<template>
  <v-container>
    <v-card class="mb-4">
      <v-tabs>
        <v-tab v-for="link in links" :key="link.to" :to="link.to">
          {{ link.title }}
        </v-tab>
      </v-tabs>
    </v-card>
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
    title: 'API Keys',
    to: '/account/api-keys',
  },
]

export default defineComponent({
  head: {
    title: 'Account',
  },
  setup() {
    return {
      links,
    }
  },
})
</script>
