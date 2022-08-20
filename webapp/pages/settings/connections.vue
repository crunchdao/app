<template>
  <div>
    <v-card-title>
      Connections
      <v-spacer />
      <connection-button-disconnect-all @disconnect="fetch" />
    </v-card-title>
    <v-divider />
    <v-card-subtitle>
      Connect accounts to your CrunchDAO account.
    </v-card-subtitle>
    <v-card outlined class="mb-4">
      <v-card-actions v-if="availableHandlers.length">
        <connection-button-connect
          v-for="type in availableHandlers"
          :key="type"
          :type="type"
        />
      </v-card-actions>
      <v-card-subtitle v-else class="text-center">
        All connection linked.
      </v-card-subtitle>
    </v-card>
    <v-card outlined>
      <v-skeleton-loader
        v-if="fetchState.pending"
        type="list-item-avatar-two-line@3"
      />
      <template v-else-if="connections.length">
        <v-list-item v-for="connection in connections" :key="connection.type">
          <v-list-item-avatar tile>
            <v-img
              :src="
                require(`@/assets/connection/${connection.type.toLowerCase()}.svg`)
              "
              max-height="32"
              max-width="32"
            />
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title>
              {{ connection.type }}
            </v-list-item-title>
            <v-list-item-subtitle>
              <span>{{ connection.username }}</span> <br />
              <code>{{ connection.handle }}</code>
            </v-list-item-subtitle>
          </v-list-item-content>
          <v-list-item-action>
            <v-btn v-if="connection.public" text outlined>
              <v-icon left>mdi-eye-off</v-icon>
              Hide off profile
            </v-btn>
            <v-btn v-else text outlined>
              <v-icon left>mdi-eye</v-icon>
              Display on profile
            </v-btn>
          </v-list-item-action>
          <v-list-item-action>
            <connection-button-disconnect
              :connection="connection"
              @disconnect="fetch"
            />
          </v-list-item-action>
        </v-list-item>
      </template>
      <v-card-subtitle v-else class="text-center">
        No connection found.
      </v-card-subtitle>
    </v-card>
    <v-pagination
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
  defineComponent,
  Ref,
  ref,
  useAsync,
  useContext,
  useFetch,
  watch,
} from '@nuxtjs/composition-api'
import { fixedComputed } from '@/composables/hack'
import { Connection, PageResponse } from '@/models'

export default defineComponent({
  layout: 'settings',
  setup() {
    const { $axios } = useContext()

    const handlers = ref([] as Array<string>)

    const page = ref(1)
    const response = ref() as Ref<PageResponse<Connection>>

    const { fetch, fetchState } = useFetch(async () => {
      handlers.value = await $axios.$get('/v1/connections/handlers')
      response.value = await $axios.$get('/v1/connections', {
        params: {
          page: page.value - 1,
          size: 8,
          sort: 'createdAt,desc',
        },
      })
    })

    const connections = fixedComputed(() => response.value?.content || [])
    const totalPages = fixedComputed(() => response.value?.totalPages || 1)
    const totalElements = fixedComputed(
      () => response.value?.totalElements || 0
    )

    watch(page, () => {
      fetch()
    })

    const availableHandlers = fixedComputed(() => {
      const connected = connections.value.map(({ type }) => type)

      return handlers.value.filter((type) => !connected.includes(type)).sort()
    })

    return {
      availableHandlers,
      connections,
      page,
      totalPages,
      totalElements,
      fetch,
      fetchState,
    }
  },
})
</script>
