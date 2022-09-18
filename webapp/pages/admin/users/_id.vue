<template>
  <v-layout v-if="!user" align-center justify-center fill-height>
    <v-progress-circular v-if="fetchState.pending" indeterminate />
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
    <nuxt-child :user="user" :user-id="userId" />
  </div>
</template>

<script lang="ts">
import {
  computed,
  defineComponent,
  ref,
  useContext,
  useFetch,
  useRoute,
  useRouter,
  watch,
} from '@nuxtjs/composition-api'
import { PageResponse, User } from '~/models'
import debounce from 'lodash.debounce'
import { fixedComputed } from '~/composables/hack'
import { extractMessage } from '~/utilities/error'

export default defineComponent({
  head: {},
  setup() {
    const { $axios } = useContext()

    const user = ref<User>()

    const route = useRoute()
    const userId = fixedComputed(() => route.value.params.id)

    const { fetch, fetchState } = useFetch(async () => {
      user.value = await $axios.$get(`/v1/users/${userId.value}`)
    })

    const error = fixedComputed(() => extractMessage(fetchState.error))

    return {
      user,
      userId,
      fetchState,
      fetch,
      error,
    }
  },
})
</script>
