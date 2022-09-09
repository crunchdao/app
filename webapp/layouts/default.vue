<template>
  <v-app>
    <app-drawer />
    <v-main>
      <nuxt />
    </v-main>
    <app-footer />
    <client-only>
      <notification-drawer />
    </client-only>
  </v-app>
</template>
