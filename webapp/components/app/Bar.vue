<template>
  <v-app-bar app clipped-left>
    <v-app-bar-title>
      <router-link to="/">
        <v-img :src="require('@/assets/logo.png')" />
      </router-link>
    </v-app-bar-title>
    <v-spacer />

    <v-list-item
      v-for="(link, index) in links"
      :key="index"
      class="flex-grow-0 mr-2"
      style="flex-basis: 0%"
      :to="link.to"
      :href="link.href"
      :target="link.target"
    >
      <v-list-item-title> {{ link.title }} </v-list-item-title>
    </v-list-item>
    <template v-if="loggedIn">
      <notification-drawer />
      <account-menu />
    </template>
    <template v-else>
      <auth-button-register class="mr-2" />
      <auth-button-login />
    </template>
  </v-app-bar>
</template>

<script lang="ts">
import { defineComponent, useContext } from '@nuxtjs/composition-api'

const links = [
  {
    title: 'Trade my Model',
    to: '/trade',
  },
  {
    title: 'My Performance',
    to: '/performance',
  },
  {
    title: 'Join Discord',
    href: 'https://discord.com/invite/veAtzsYn3M',
    target: '_blank',
  },
  {
    title: 'Wiki',
    href: 'http://docs.crunchdao.com',
    target: '_blank',
  },
]

export default defineComponent({
  setup() {
    const { $auth } = useContext()

    const { loggedIn } = $auth

    return { loggedIn, links }
  },
})
</script>
