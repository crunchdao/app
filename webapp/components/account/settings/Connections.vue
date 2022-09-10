<template>
  <v-card id="connections">
    <card-title>
      Social Media Connections
      <template #action>
        <connection-button-disconnect-all @disconnect="fetch" />
      </template>
    </card-title>
    <v-card-subtitle>
      Connect your social media accounts to achieve the "Associate" level
      requirement.
    </v-card-subtitle>
    <v-skeleton-loader
      v-if="fetchState.pending"
      type="list-item-avatar-two-line@3"
    />
    <template v-else>
      <connection-item
        v-for="pair in pairs"
        :key="pair.handler.type"
        :connection="pair.connection"
        :handler="pair.handler"
        @update="onUpdate"
        @disconnect="fetch"
        @connect="fetch"
      />
    </template>
  </v-card>
</template>

<script lang="ts">
import {
  computed,
  defineComponent,
  ref,
  useContext,
  useFetch,
} from '@nuxtjs/composition-api'
import { Connection, ConnectionHandler } from '@/models'
import groupBy from 'lodash.groupby'
import { fixedComputed } from '~/composables/hack'

export default defineComponent({
  setup() {
    const { $axios } = useContext()

    const handlers = ref<Array<ConnectionHandler>>([])
    const connections = ref<Array<Connection>>([])

    const { fetch, fetchState } = useFetch(async () => {
      handlers.value = await $axios.$get('/v1/connections/handlers')
      connections.value = await $axios.$get('/v1/connections')
    })

    const pairs = fixedComputed(() => {
      const byType = groupBy(connections.value, ({ type }) => type)

      return handlers.value.map((handler) => ({
        handler,
        connection: byType[handler.type]?.[0] || null,
      }))
    })

    const onUpdate = (connection: Connection) => {
      const target = connections.value.find(
        ({ type }) => type === connection.type
      )

      if (target) {
        Object.assign(target, connection)
      }
    }

    return {
      pairs,
      connections,
      fetch,
      fetchState,
      onUpdate,
    }
  },
})
</script>
