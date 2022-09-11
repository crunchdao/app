<template>
  <v-layout v-if="!user" align-center justify-center fill-height>
    <v-progress-circular v-if="$fetchState.pending" indeterminate />
    <v-flex v-else style="max-width: 400px">
      <v-alert type="error">
        Could not load the page: <br />
        <code>{{ error }}</code>
      </v-alert>
      <v-btn block @click="fetch">
        <v-icon left>mdi-refresh</v-icon>
        retry
      </v-btn>
    </v-flex>
  </v-layout>
  <div v-else>
    <nuxt-child :user="user" :user-id="userId" :is-yourself="isYourself" />
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import { User, UUID } from '~/models'
import { extractMessage } from '~/utilities/error'

export default Vue.extend({
  head: {},
  data: () => ({
    user: null as User | null,
  }),
  computed: {
    userId(): UUID {
      return this.$route.params.id
    },
    isYourself(): boolean {
      return this.$auth.user?.id === this.userId
    },
    error(): string {
      return extractMessage(this.$fetchState.error)
    },
  },
  async fetch() {
    this.user = await this.$axios.$get(`/v1/users/${this.userId}`)
  }
})
</script>
