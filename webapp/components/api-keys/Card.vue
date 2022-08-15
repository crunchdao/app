<template>
  <v-card :loading="fetchState.pending">
    <v-card-title>
      API Keys
      <template v-if="totalElements">({{ totalElements }})</template>
      <v-spacer />
      <v-btn icon :loading="fetchState.pending" @click="fetch">
        <v-icon>mdi-refresh</v-icon>
      </v-btn>
      <api-keys-dialog-create @create="fetch">
        <template #activator="{ on, attrs }">
          <v-btn icon class="ml-2" v-bind="attrs" v-on="on">
            <v-icon>mdi-plus</v-icon>
          </v-btn>
        </template>
      </api-keys-dialog-create>
      <client-only>
        <v-menu offset-x right offset-y>
          <template #activator="{ on, attrs }">
            <v-btn icon class="ml-2" v-bind="attrs" v-on="on">
              <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>
          </template>
          <v-list>
            <api-keys-button-delete-all @delete="fetch" />
          </v-list>
        </v-menu>
      </client-only>
    </v-card-title>
    <template v-if="apiKeys.length">
      <v-list>
        <api-keys-item
          v-for="apiKey in apiKeys"
          :key="apiKey.id"
          :api-key="apiKey"
          @delete="fetch"
        />
      </v-list>
      <v-pagination
        v-model="page"
        :length="totalPages"
        :total-visible="7"
        :disabled="fetchState.pending"
      />
    </template>
    <v-card-subtitle v-else class="text-center">
      No API Key found.
    </v-card-subtitle>
  </v-card>
</template>

<script lang="ts">
import {
  computed,
  defineComponent,
  Ref,
  ref,
  useContext,
  useFetch,
  watch,
} from '@nuxtjs/composition-api'
import { fixedComputed } from '@/composables/hack'
import { ApiKey, PageResponse } from '@/models'

export default defineComponent({
  setup() {
    const { $axios } = useContext()

    const page = ref(1)
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
    const totalPages = fixedComputed(() => response.value?.totalPages || 1)
    const totalElements = fixedComputed(
      () => response.value?.totalElements || 0
    )

    watch(page, () => {
      fetch()
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
