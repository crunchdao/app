<template>
  <div>
    <v-card-title>
      API Keys
      <v-spacer />
      <v-btn class="mr-2" text outlined to="/settings/api-keys/create">
        <v-icon left>mdi-plus</v-icon>
        Add API-Key
      </v-btn>
      <api-keys-button-revoke-all @delete="fetch" />
    </v-card-title>
    <v-divider />
    <v-card-subtitle>
      API Key you have generated that can be used to access the CrunchDAO API.
    </v-card-subtitle>
    <v-card outlined>
      <v-skeleton-loader
        v-if="fetchState.pending"
        type="list-item-avatar-two-line@3"
      />
      <template v-else-if="apiKeys.length">
        <api-keys-item
          v-for="apiKey in apiKeys"
          :key="apiKey.id"
          :api-key="apiKey"
          @delete="fetch"
        />
      </template>
      <v-card-subtitle v-else class="text-center">
        No API Key found.
      </v-card-subtitle>
    </v-card>
    <v-pagination
      v-if="totalPages"
      v-model="page"
      :length="totalPages"
      :total-visible="7"
      :disabled="fetchState.pending"
      class="mt-2"
    />
  </div>
</template>

<script lang="ts">
import {
  computed,
  defineComponent,
  Ref,
  ref,
  useContext,
  useFetch,
  useRoute,
  useRouter,
  watch,
} from '@nuxtjs/composition-api'
import { fixedComputed } from '@/composables/hack'
import { ApiKey, PageResponse } from '@/models'

export default defineComponent({
  layout: 'settings',
  setup() {
    const { $axios } = useContext()
    const router = useRouter()
    const route = useRoute()

    const page = computed({
      get() {
        const value = parseInt(route.value.query.page as string)

        if (isNaN(value)) {
          return 1
        }

        return value
      },
      set(value: number) {
        router.push({
          query: {
            page: String(value),
          },
        })
      },
    })

    const response = ref() as Ref<PageResponse<ApiKey>>
    const { fetch, fetchState } = useFetch(async () => {
      response.value = await $axios.$get('/v1/api-keys', {
        params: {
          page: page.value - 1,
          size: 10,
          sort: 'createdAt,desc',
        },
      })
    })

    const apiKeys = fixedComputed(() => response.value?.content || [])
    const totalPages = fixedComputed(() => response.value?.totalPages || 0)
    const totalElements = fixedComputed(
      () => response.value?.totalElements || 0
    )

    watch(page, () => {
      fetch()
    })

    watch([apiKeys, totalPages, totalElements], () => {
      if (!apiKeys.value.length && totalElements.value && page.value > 1) {
        page.value -= 1
        fetch()
      }
    })

    return {
      apiKeys,
      page,
      totalPages,
      totalElements,
      fetch,
      fetchState,
    }
  },
})
</script>
