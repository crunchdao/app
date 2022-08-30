<template>
  <div>
    <v-btn icon class="mx-1" @click="drawer = true">
      <v-icon>mdi-bell</v-icon>
    </v-btn>
    <v-navigation-drawer
      v-model="drawer"
      absolute
      app
      temporary
      right
      disable-resize-watcher
      disable-route-watcher
      width="360"
    >
      <template #prepend>
        <v-card-title> Notifications </v-card-title>
        <v-card-actions class="pt-0">
          <v-btn class="grow" outlined text color="primary" small>
            <v-icon left>mdi-close</v-icon>
            Read All
          </v-btn>
          <v-btn
            class="grow"
            outlined
            text
            color="info"
            small
            to="/settings/notifications"
          >
            <v-icon left>mdi-cog</v-icon>
            Settings
          </v-btn>
        </v-card-actions>

        <v-divider />
      </template>

      <v-list>
        <v-list-item
          v-for="notification in notifications"
          :key="notification.id"
          :to="notification.to"
          exact
          @click="markAsRead(notification)"
        >
          <v-list-item-avatar>
            <v-icon>{{ notification.icon }}</v-icon>
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title>{{ notification.title }}</v-list-item-title>
            <v-list-item-subtitle>{{
              notification.subtitle
            }}</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
      </v-list>

      <template #append>
        <v-divider />
        <v-list-item>
          <v-btn block to="/notifications"> All Notifications </v-btn>
        </v-list-item>
      </template>
    </v-navigation-drawer>
  </div>
</template>

<script lang="ts">
import {
  computed,
  defineComponent,
  ref,
  useContext,
  useFetch,
} from '@nuxtjs/composition-api'
import { useNotificationStore } from '~/store/notifications'
import { handlers } from '@/utilities/notification'

export default defineComponent({
  setup() {
    const { $axios } = useContext()

    const notificationStore = useNotificationStore()

    const { fetch } = useFetch(async () => {
      notificationStore.response = await $axios.$get(`/v1/notifications`)
    })

    const notifications = notificationStore.notifications
      .map((notification) => handlers[notification.type]?.(notification))
      .filter((notification) => !!notification)

    return {
      drawer: computed<boolean>({
        get() {
          return notificationStore.drawer
        },
        set(value) {
          notificationStore.drawer = value
        },
      }),
      notifications,
      markAsRead: notificationStore.markAsRead,
      fetch,
    }
  },
})
</script>
