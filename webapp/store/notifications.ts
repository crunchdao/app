import { defineStore } from 'pinia'
import { PageResponse, Notification } from "@/models"
import { computed, ref } from 'vue'
import { fixedComputed } from '~/composables/hack'
import { useContext } from '@nuxtjs/composition-api'

export const useNotificationStore = defineStore('notification', () => {
  const { $axios } = useContext()

  const drawer = ref(false)
  const response = ref<PageResponse<Notification>>()
  const notifications = fixedComputed(() => response.value?.content || [])

  const markAsRead = async ({ id }: Notification) => {
    try {
      const notification = await $axios.$delete(`/v1/notifications/${id}`)

      const index = notifications.value.findIndex(({ id: id_ }) => id_ == id)
      if (index != -1) {
        Object.assign(notifications.value[index], notification)
      }
    } catch (error: any) {
      console.error("Could not mark as read", error)
    }
  }

  return {
    drawer,
    response,
    notifications,
    markAsRead,
  }
})