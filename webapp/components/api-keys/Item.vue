<template>
  <v-list-item :to="to">
    <v-list-item-avatar>
      <v-icon>mdi-key</v-icon>
    </v-list-item-avatar>
    <v-list-item-content>
      <v-list-item-title>
        {{ apiKey.name }}
      </v-list-item-title>
      <v-list-item-subtitle>
        <template v-if="apiKey.description">
          {{ apiKey.description }}
        </template>
      </v-list-item-subtitle>
    </v-list-item-content>
    <v-list-item-action-text>
      <v-hover v-slot="{ hover }">
        <v-text-field
          class="d-inline-block"
          dense
          hide-details
          readonly
          outlined
          style="font-family: monospace; width: 100px"
          label="hint"
          :value="(hover ? apiKey.truncated : '••••') + '••••'"
        />
      </v-hover>
    </v-list-item-action-text>
    <v-list-item-action>
      <api-keys-button-revoke :api-key="apiKey" @delete="onDelete" />
    </v-list-item-action>
  </v-list-item>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'
import { ApiKey } from '@/models'
import { fixedComputed } from '~/composables/hack'

export default defineComponent({
  props: {
    apiKey: {
      required: true,
    } as Vue.PropOptions<ApiKey>,
  },
  emits: {
    delete: () => true,
  },
  setup(props, { emit }) {
    const { apiKey } = props

    const onDelete = async () => {
      emit('delete')
    }

    const to = fixedComputed(() => `/settings/api-keys/${apiKey!.id}`)

    return { apiKey, onDelete, to }
  },
})
</script>
