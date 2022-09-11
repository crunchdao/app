<template>
  <v-list-item v-if="connection" :href="connection.profileUrl" target="_blank" @click="onClick">
    <v-list-item-avatar tile>
      <connection-icon :type="handler.type" />
    </v-list-item-avatar>
    <v-list-item-content>
      <v-list-item-title>
        {{ handler.name }}
        <v-chip v-if="connection.public" x-small color="success">public</v-chip>
        <v-chip v-else x-small>private</v-chip>
      </v-list-item-title>
      <v-list-item-subtitle>
        <span>{{ connection.username }}</span> <br />
        <code>{{ connection.handle }}</code>
      </v-list-item-subtitle>
    </v-list-item-content>
    <v-list-item-action>
      <connection-button-public-toggle
        :connection="connection"
        @update="onUpdate"
      />
    </v-list-item-action>
    <v-list-item-action>
      <connection-button-disconnect
        :connection="connection"
        @disconnect="onDisconnect"
      />
    </v-list-item-action>
  </v-list-item>
  <v-list-item v-else link>
    <v-list-item-avatar tile>
      <connection-icon :type="handler.type" />
    </v-list-item-avatar>
    <v-list-item-content>
      <v-list-item-title>
        {{ handler.name }}
      </v-list-item-title>
    </v-list-item-content>
    <v-list-item-action>
      <connection-button-connect
        :handler="handler"
      />
    </v-list-item-action>
  </v-list-item>
</template>

<script lang="ts">
import { defineComponent, PropType, toRefs, useContext } from '@nuxtjs/composition-api'
import { Connection, ConnectionHandler } from '@/models'
import copyToClipboard from 'copy-to-clipboard'

export default defineComponent({
  props: {
    connection: {
      type: Object as PropType<Connection | null>,
    },
    handler: {
      type: Object as PropType<ConnectionHandler>,
      required: true,
    },
  },
  emits: {
    update: (_connection: Connection) => true,
    disconnect: (_connection: Connection) => true,
  },
  setup(props, { emit }) {
    const { $dialog } = useContext()
    const { connection } = toRefs(props)

    const onUpdate = (connection: Connection) => {
      emit('update', connection)
    }

    const onDisconnect = (connection: Connection) => {
      emit('disconnect', connection)
    }

    const onClick = () => {
      if (connection.value && !connection.value.profileUrl) {
        copyToClipboard(connection.value.handle)
        $dialog.notify.success('Copied!')
      }
    }

    return {
      connection,
      onUpdate,
      onDisconnect,
      onClick,
    }
  },
})
</script>
