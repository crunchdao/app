<template>
  <v-navigation-drawer
    mini-variant
    fixed
    app
    expand-on-hover
    disable-resize-watcher
    permanent
  >
    <v-list>
      <v-list-item
        v-for="link in links"
        :key="link.to"
        :to="link.to"
        router
        exact
      >
        <v-list-item-action>
          <v-icon>{{ link.icon }}</v-icon>
        </v-list-item-action>
        <v-list-item-content>
          <v-list-item-title v-text="link.title" />
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script lang="ts">
import { defineComponent, ref } from '@nuxtjs/composition-api'

const links = [
  {
    icon: 'mdi-application',
    title: 'Hello',
    to: '/',
  },
]

export default defineComponent({
  setup() {
    return { links }
  },
})
</script>
