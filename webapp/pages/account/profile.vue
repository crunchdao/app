<template>
  <div>
    <settings-title title="Public profile" />
    <v-row>
      <v-col>
        <v-subheader> Profile </v-subheader>
        <v-skeleton-loader
          boilerplate
          type="table-heading, list-item-two-line, table-tfoot"
        />
      </v-col>
      <v-col>
        <v-subheader> Avatar </v-subheader>
        <avatar-editor />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  head: {
    title: 'Profile Settings',
  },
})
</script>
