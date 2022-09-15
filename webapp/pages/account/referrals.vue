<template>
  <v-card>
    <card-title> Referrals </card-title>
    <v-card-subtitle v-if="fetchState.pending">
      <v-skeleton-loader type="text" />
    </v-card-subtitle>
    <v-card-subtitle v-else-if="referralUrl">
      Your referral link:
      <a :href="referralUrl" target="_blank">{{ referralUrl }}</a>
    </v-card-subtitle>
    <v-skeleton-loader v-if="fetchState.pending" type="list-item-avatar@3" />
    <template v-else-if="pageResponse && pageResponse.totalElements">
      <v-list>
        <v-list-item
          v-for="referral in pageResponse.content"
          :key="referral.user.id"
          :to="toUser(referral.user)"
        >
          <v-list-item-avatar>
            <avatar :user-id="referral.user.id" />
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title>{{ referral.user.username }}</v-list-item-title>
            <v-list-item-subtitle>
              Referred at
              <code>{{ new Date(referral.createdAt).toLocaleString() }}</code
              >, using the code <code>{{ referral.code }}</code>
            </v-list-item-subtitle>
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
    <v-card-text v-else> you didn't referred anyone </v-card-text>
  </v-card>
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
import { fixedComputed } from '~/composables/hack'
import { PageResponse, Referral, User } from '~/models'

export default defineComponent({
  head: {
    title: 'Referrals',
  },
  setup() {
    const { $axios } = useContext()

    const route = useRoute()

    const page = computed(() => parseInt(route.value.query.page as string) || 1)

    const pageResponse = ref<PageResponse<Referral>>()
    const referralCode = ref<string>()

    const { fetch, fetchState } = useFetch(async () => {
      const response = await $axios.$post(`/graphql`, {
        query: `
          query ($page: Int!) {
            myReferralCode {
              value
            }
            referrals(page: $page) {
              content {
                user {
                  id
                  username
                }
                code
                validated
                validatedAt
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
          page: page.value - 1,
        },
      })

      referralCode.value = response.data.myReferralCode?.value
      pageResponse.value = response.data.referrals
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

    const referralUrl = fixedComputed(() => {
      if (!referralCode.value) {
        return null
      }

      const part = `/auth/register?referral=${referralCode.value}`
      if (process.client) {
        return `${window.location.origin}${part}`
      }

      return part
    })

    return {
      fetchState,
      pageResponse,
      referralUrl,
      toUser,
      changePage,
    }
  },
})
</script>
