<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-title> Account </v-card-title>
          <v-card-text>
            <pre>{{ user }}</pre>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import { defineComponent, useContext } from '@nuxtjs/composition-api'

export default defineComponent({
  head: {
    title: 'Account',
  },
  setup() {
    const { user } = useContext().$auth

    return { user }
  },
})
</script>
