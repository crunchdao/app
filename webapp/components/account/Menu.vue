<template>
  <v-menu bottom offset-y>
    <template #activator="{ on, attrs }">
      <v-btn icon class="mr-2" v-bind="attrs" v-on="on">
        <v-avatar size="32">
          <img src="https://cdn.vuetifyjs.com/images/john.jpg" alt="John" />
        </v-avatar>
      </v-btn>
    </template>
    <v-list class="py-0">
      <v-list-item v-for="link in links" :key="link.to" :to="link.to">
        <v-list-item-avatar>
          <v-icon> {{ link.icon }} </v-icon>
        </v-list-item-avatar>
        <v-list-item-title> {{ link.title }} </v-list-item-title>
      </v-list-item>
      <v-divider />
      <v-list-item to="/auth/logout">
        <v-list-item-avatar>
          <v-icon> mdi-logout </v-icon>
        </v-list-item-avatar>
        <v-list-item-title> Log-out </v-list-item-title>
      </v-list-item>
    </v-list>
  </v-menu>
</template>

<script lang="ts">
import { defineComponent, useContext } from '@nuxtjs/composition-api'

const links = [
  {
    icon: 'mdi-account',
    title: 'Manage account',
    to: '/account',
  },
  {
    icon: 'mdi-cog',
    title: 'Settings',
    to: '/settings',
  },
]

export default defineComponent({
  setup() {
    const { $auth } = useContext()

    const username = $auth.user?.username

    return { username, links }
  },
})
</script>
