<template>
  <v-card-title class="text-uppercase">
    <slot />
    <v-spacer />
    <slot name="action">
      <v-btn style="visibility: hidden" />
    </slot>
  </v-card-title>
</template>
