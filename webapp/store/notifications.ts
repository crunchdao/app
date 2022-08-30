import { defineStore } from 'pinia'
import { PageResponse, Notification } from "@/models"
import { computed, ref } from 'vue'
import { fixedComputed } from '~/composables/hack'
import { useContext } from '@nuxtjs/composition-api'

export const useNotificationStore = defineStore('notification', () => {
  const { $axios } = useContext()

  const drawer = ref(false)
  const response = ref<PageResponse<Notification>>()
  const notifications = computed(() => response.value?.content || [])

  const loadingCount = ref(0)
  const loading = computed(() => loadingCount.value != 0)

  const fetch = async () => {
    try {
      loadingCount.value++

      response.value = await $axios.$get(`/v1/notifications`, {
        params: {
          onlyUnread: true,
          sort: "createdAt,desc"
        }
      })
    } catch (error: any) {
      console.error("Could not fetch notifications", error)
    } finally {
      loadingCount.value--
    }
  }

  const markAsRead = async ({ id }: Notification) => {
    try {
      loadingCount.value++

      const notification = await $axios.$delete(`/v1/notifications/${id}`)

      const index = notifications.value.findIndex(({ id: id_ }) => id_ == id)
      if (index != -1) {
        Object.assign(notifications.value[index], notification)
      }
    } catch (error: any) {
      console.error("Could not mark as read", error)
    } finally {
      loadingCount.value--
    }
  }

  const hasMore = fixedComputed(() => (response.value?.totalElements || 0) !== 0)
  const hasMoreText = fixedComputed(() => {
    let text = (response.value?.totalElements || 0).toString()

    if (response.value?.last === false) {
      text += "+"
    }

    return text
  });

  if (process.client) {
    setInterval(fetch, 30_000)
  }

  return {
    drawer,
    loading,
    notifications,
    fetch,
    markAsRead,
    hasMore,
    hasMoreText
  }
})