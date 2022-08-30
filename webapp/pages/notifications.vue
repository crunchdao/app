<template>
  <pre @click="fetch"> {{ notifications }} </pre>
</template>

<script lang="ts">
import { defineComponent, ref, useContext, useFetch } from '@nuxtjs/composition-api'
import { useNotificationStore } from "@/store/notifications"

export default defineComponent({
  head: {
    title: 'Notifications',
  },
  setup() {
    const notificationStore = useNotificationStore()

    return {
      notifications: notificationStore.notifications,
      fetch
    }
  },
})
</script>
