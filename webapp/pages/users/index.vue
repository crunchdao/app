<template>
  <v-container>
    <v-card>
      <v-skeleton-loader
        v-if="fetchState.pending"
        type="list-item-avatar@3"
      />
      <v-list v-else>
        <v-list-item v-for="user in users" :key="user.id" :to="toUser(user)">
          <v-list-item-avatar>
            <avatar :user-id="user.id" />
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title>{{ user.username }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  useContext,
  useFetch,
} from '@nuxtjs/composition-api'
import { User } from '~/models'

export default defineComponent({
  head: {
    title: 'Users',
  },
  setup() {
    const { $axios } = useContext()

    const users = ref<Array<User>>([])
    const { fetchState } = useFetch(async () => {
      users.value = (await $axios.$get(`/v1/users/`)).content
    })

    const toUser = ({ id }: User) => {
      return `/users/${id}`
    }

    return {
      users,
      toUser,
      fetchState,
    }
  },
})
</script>
