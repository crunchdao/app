<template>
  <v-container>
    <v-card>
      <card-title>
        Users
        <template #action>
          <v-text-field
            v-model="query"
            label="Search"
            outlined
            dense
            hide-details
            style="max-width: 220px"
            prepend-inner-icon="mdi-magnify"
            clearable
            @blur="fetch"
          />
        </template>
      </card-title>
      <v-skeleton-loader v-if="fetchState.pending" type="list-item-avatar@3" />
      <template v-else-if="pageResponse && pageResponse.totalElements">
        <v-list>
          <v-list-item
            v-for="user in pageResponse.content"
            :key="user.id"
            :to="toUser(user)"
          >
            <v-list-item-avatar>
              <avatar :user-id="user.id" />
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title>{{ user.username }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
        <v-pagination
          :value="pageResponse.pageNumber + 1"
          :length="pageResponse.totalPages"
          :total-visible="7"
          :disabled="fetchState.pending"
          class="mt-2"
          @input="changePage"
        />
      </template>
      <v-card-text v-else> No user matching criterias. </v-card-text>
    </v-card>
  </v-container>
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

export default defineComponent({
  head: {
    title: 'Users',
  },
  setup() {
    const { $axios } = useContext()

    const route = useRoute()
    const page = computed(() => parseInt(route.value.query.page as string) || 1)

    const query = computed({
      get() {
        return route.value.query.query
      },
      set(value) {
        router.replace({
          query: {
            query: value as string,
          },
        })
      },
    })

    const pageResponse = ref<PageResponse<User>>()
    const { fetch, fetchState } = useFetch(async () => {
      pageResponse.value = await $axios.$get(`/v1/users/`, {
        params: {
          username: query.value || undefined,
          page: page.value - 1,
        },
      })
    })

    const toUser = ({ id }: User) => {
      return `/admin/users/${id}`
    }

    watch(page, fetch)

    const router = useRouter()
    const changePage = (newPage: number) => {
      router.push({
        query: {
          page: newPage.toString(),
        },
      })
    }

    watch(query, debounce(fetch, 600))

    return {
      pageResponse,
      toUser,
      fetchState,
      fetch,
      changePage,
      query,
    }
  },
})
</script>
