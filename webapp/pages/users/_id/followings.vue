<template>
  <v-container>
    <v-card>
      <card-title> {{ user.username }}'s followings </card-title>
      <v-skeleton-loader v-if="fetchState.pending" type="list-item-avatar@3" />
      <template v-else-if="pageResponse && pageResponse.totalElements">
        <v-list>
          <v-list-item
            v-for="{ user } in pageResponse.content"
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
      <v-card-text v-else>
        {{ user.username }} hasn't followed anyone yet.
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import {
  computed,
  defineComponent,
  PropType,
  ref,
  toRefs,
  useContext,
  useFetch,
  useRoute,
  useRouter,
  watch,
  watchEffect,
} from '@nuxtjs/composition-api'
import { Follow, PageResponse, User } from '~/models'

export default defineComponent({
  head: {
    title: 'Followings',
  },
  props: {
    user: {
      type: Object as PropType<User>,
      required: true,
    },
  },
  setup(props) {
    const { $axios } = useContext()
    const { user } = toRefs(props)

    const route = useRoute()

    const page = computed(() => parseInt(route.value.query.page as string) || 1)

    const pageResponse = ref<PageResponse<Follow>>()
    const { fetch, fetchState } = useFetch(async () => {
      const response = await $axios.$post(`/graphql`, {
        query: `
          query ($userId: UUID!, $page: Int!) {
            followings(userId: $userId, page: $page) {
              content {
                user {
                  id
                  username
                }
                createdAt
              }
              pageNumber
              pageSize
              totalElements
              totalPages
              first
              last
            }
          }
        `,
        variables: {
          userId: user.value.id,
          page: page.value - 1,
        },
      })

      pageResponse.value = response.data.followings
    })

    watch(page, fetch)

    const toUser = ({ id }: User) => {
      return `/users/${id}`
    }

    const router = useRouter()
    const changePage = (newPage: number) => {
      router.push({
        query: {
          page: newPage.toString(),
        },
      })
    }

    return {
      fetchState,
      user,
      pageResponse,
      toUser,
      changePage,
    }
  },
})
</script>
