<template>
  <div>
    <v-app-bar v-if="!drawer" app>
      <v-app-bar-nav-icon @click="openDrawer" />
    </v-app-bar>
    <v-navigation-drawer
      v-model="drawer"
      app
      floating
      style="background-color: transparent"
      class="flex pa-3 pr-2"
    >
      <v-card class="d-flex flex-column fill-height" style="overflow: auto">
        <app-logo class="flex-grow-0" />
        <div class="d-flex flex-column flex-grow-1">
          <v-flex
            class="
              d-flex
              flex-column flex-grow-0
              align-center
              justify-center
              mt-4
              mb-2
            "
          >
            <template v-if="loggedIn">
              <avatar :user-id="user.id" size="80" />
              <span class="text-h5">{{ user.username }}</span>
              <small class="text--secondary text-uppercase">
                Intern • 12 Followers
              </small>
            </template>
            <template v-else>
              <auth-button-login />
              <small class="mt-3"> Don't have an account? </small>
              <auth-button-register />
            </template>
          </v-flex>

          <v-divider />

          <v-flex class="d-flex flex-column flex-grow-1">
            <div style="flex-grow: 1" />
            <v-list nav style="flex-grow: 0">
              <v-list-item
                v-for="link in links"
                :key="link.to"
                :to="link.to"
                :exact-path="link.exactPath"
                :disabled="link.disabled"
              >
                <v-list-item-icon>
                  <v-icon>{{ link.icon }}</v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>{{ link.title }}</v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list>
            <div style="flex-grow: 3" />
          </v-flex>
        </div>

        <v-divider />

        <v-flex class="d-flex flex-grow-0 ma-4 align-center">
          <token-price />
          <client-only>
            <notification-button-open-drawer />
          </client-only>
          <auth-button-logout v-if="loggedIn" icon />
        </v-flex>
      </v-card>
    </v-navigation-drawer>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref } from '@nuxtjs/composition-api'
import { useAuth } from '@/composables/auth'

export default defineComponent({
  setup() {
    const { user, loggedIn } = useAuth()

    const links = computed(() => [
      {
        title: 'Leaderboard',
        icon: 'mdi-poll',
        to: '/',
        exactPath: true,
        disabled: false,
      },
      {
        title: 'Performance',
        icon: 'mdi-home',
        to: '/performance',
        exactPath: false,
        disabled: !loggedIn.value,
      },
      {
        title: 'Wallet',
        icon: 'mdi-wallet',
        to: '/wallet',
        exactPath: false,
        disabled: !loggedIn.value,
      },
      {
        title: 'Account',
        icon: 'mdi-account',
        to: '/account',
        exactPath: false,
        disabled: !loggedIn.value,
      },
      {
        title: 'Users',
        icon: 'mdi-account',
        to: '/users',
        exactPath: false,
        disabled: false,
      },
    ])

    const drawer = ref(true)

    const openDrawer = () => {
      drawer.value = true
    }

    return {
      user,
      loggedIn,
      links,
      drawer,
      openDrawer,
    }
  },
})
</script>
