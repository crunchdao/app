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
      requirement
    </v-card-subtitle>
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
      <v-skeleton-loader
        v-if="fetchState.pending"
        type="list-item-avatar-two-line@3"
      />
      <template v-else-if="connections.length">
        <connection-item
          v-for="connection in connections"
          :key="connection.type"
          :connection="connection"
          @update="onUpdate"
          @disconnect="fetch"
        />
      </template>
      <v-card-subtitle v-else class="text-center">
        No connection found.
      </v-card-subtitle>
  </v-card>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  useContext,
  useFetch,
} from '@nuxtjs/composition-api'
import { fixedComputed } from '@/composables/hack'
import { Connection } from '@/models'

export default defineComponent({
  head: {
    title: 'Connections',
  },
  setup() {
    const { $axios } = useContext()

    const handlers = ref<Array<string>>([])
    const connections = ref<Array<Connection>>([])

    const { fetch, fetchState } = useFetch(async () => {
      handlers.value = await $axios.$get('/v1/connections/handlers')
      connections.value = await $axios.$get('/v1/connections')
    })

    const availableHandlers = fixedComputed(() => {
      const connected = connections.value.map(({ type }) => type)

      return handlers.value.filter((type) => !connected.includes(type)).sort()
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
      availableHandlers,
      connections,
      fetch,
      fetchState,
      onUpdate,
    }
  },
})
</script>
