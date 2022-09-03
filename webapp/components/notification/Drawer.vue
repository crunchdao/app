<template>
  <client-only>
    <div>
      <v-btn icon class="mx-1" :loading="loading" @click="drawer = true">
        <v-badge :value="hasMore" overlap bordered :content="hasMoreText">
          <v-icon>mdi-bell</v-icon>
        </v-badge>
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
          <v-card-title>
            Notifications
            <v-spacer />
            <v-btn icon :loading="loading" @click="fetch">
              <v-icon>mdi-refresh</v-icon>
            </v-btn>
          </v-card-title>
          <v-card-actions class="pt-0">
            <notification-button-read-all class="grow" @read-all="fetch" />
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

        <v-skeleton-loader
          v-if="loading"
          type="list-item-avatar-two-line@3"
          class="uncolored-bones"
        />
        <v-list v-else-if="notifications.length">
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
              <v-list-item-subtitle>
                {{ notification.subtitle }}
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </v-list>
        <v-card-subtitle v-else class="text-center">
          No new notification.
        </v-card-subtitle>
      </v-navigation-drawer>
    </div>
  </client-only>
</template>

<script lang="ts">
import { defineComponent, watch } from '@nuxtjs/composition-api'
import { useNotificationStore } from '~/store/notifications'
import { handlers } from '@/utilities/notification'
import { fixedComputed } from '~/composables/hack'
import { storeToRefs } from 'pinia'

export default defineComponent({
  setup() {
    const notificationStore = useNotificationStore()

    const notifications = fixedComputed(() =>
      notificationStore.notifications
        .map((notification) => handlers[notification.type]?.(notification))
        .filter((notification) => !!notification)
    )

    const { drawer } = storeToRefs(notificationStore)

    watch(drawer, (value) => {
      if (value) {
        notificationStore.fetch()
      }
    })

    const { loading, hasMore, hasMoreText } = storeToRefs(notificationStore)

    return {
      drawer,
      notifications,
      markAsRead: notificationStore.markAsRead.bind(notificationStore),
      fetch: notificationStore.fetch.bind(notificationStore),
      loading,
      hasMore,
      hasMoreText,
    }
  },
})
</script>
