<template>
  <v-list-item :href="connection.profileUrl" target="_blank" @click="onClick">
    <v-list-item-avatar tile>
      <v-img :src="icon" max-height="32" max-width="32" />
    </v-list-item-avatar>
    <v-list-item-content>
      <v-list-item-title>
        {{ connection.type }}
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
</template>

<script lang="ts">
import { defineComponent, PropType, useContext } from '@nuxtjs/composition-api'
import { fixedComputed } from '@/composables/hack'
import { Connection } from '@/models'
import copyToClipboard from 'copy-to-clipboard'

export default defineComponent({
  layout: 'settings',
  props: {
    connection: {
      type: Object as PropType<Connection>,
      required: true,
    },
  },
  emits: {
    update: (_connection: Connection) => true,
    disconnect: (_connection: Connection) => true,
  },
  setup(props, { emit }) {
    const { $dialog } = useContext()
    const { connection } = props

    const onUpdate = (connection: Connection) => {
      emit('update', connection)
    }

    const onDisconnect = (connection: Connection) => {
      emit('disconnect', connection)
    }

    const icon = fixedComputed(() =>
      require(`@/assets/connection/${connection.type.toLowerCase()}.svg`)
    )

    const onClick = () => {
      if (!connection.profileUrl) {
        copyToClipboard(connection.handle)
        $dialog.notify.success('Copied!')
      }
    }

    return {
      connection,
      icon,
      onUpdate,
      onDisconnect,
      onClick,
    }
  },
})
</script>
