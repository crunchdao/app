<template>
  <v-app>
    <app-bar />
    <v-main>
      <v-container>
        <v-row>
          <v-col cols="12" xs="12" md="3" xl="2">
            <template v-for="link in links">
              <v-list-item
                v-if="link.to"
                :key="link.title"
                :to="link.to"
                router
              >
                <v-list-item-action>
                  <v-icon>{{ link.icon }}</v-icon>
                </v-list-item-action>
                <v-list-item-content>
                  <v-list-item-title v-text="link.title" />
                </v-list-item-content>
              </v-list-item>
              <v-subheader v-else :key="link.title" v-text="link.title" />
            </template>
          </v-col>
          <v-col cols="12" xs="12" md="9" xl="10">
            <nuxt />
          </v-col>
        </v-row>
      </v-container>
    </v-main>
    <app-footer />
  </v-app>
</template>

<script lang="ts">
import { defineComponent, useContext } from '@nuxtjs/composition-api'

const links = [
  {
    title: 'User',
  },
  {
    icon: 'mdi-account',
    title: 'Public profile',
    to: '/settings/profile',
  },
  {
    icon: 'mdi-cog',
    title: 'Account',
    to: '/settings/account',
  },
  {
    icon: 'mdi-link-variant',
    title: 'Connections',
    to: '/settings/connections',
  },
  {
    icon: 'mdi-bell',
    title: 'Notifications',
    to: '/settings/notifications',
  },
  {
    title: 'Developer',
  },
  {
    icon: 'mdi-api',
    title: 'API Keys',
    to: '/settings/api-keys',
  },
]

export default defineComponent({
  setup() {
    return { links }
  },
})
</script>
