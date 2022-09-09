<template>
  <v-btn icon class="mx-1" :loading="loading" @click="drawer = true">
    <v-badge :value="hasMore" overlap bordered :content="hasMoreText">
      <v-icon>mdi-bell</v-icon>
    </v-badge>
  </v-btn>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'
import { useNotificationStore } from '~/store/notifications'
import { storeToRefs } from 'pinia'

export default defineComponent({
  setup() {
    const notificationStore = useNotificationStore()

    const { drawer, loading, hasMore, hasMoreText } =
      storeToRefs(notificationStore)

    return {
      drawer,
      loading,
      hasMore,
      hasMoreText,
    }
  },
})
</script>
