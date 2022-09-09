<template>
  <v-list-item :to="to">
    <v-list-item-avatar>
      <v-icon>mdi-key</v-icon>
    </v-list-item-avatar>
    <v-list-item-content>
      <v-list-item-title>
        {{ apiKey.name }}
        <small class="text--secondary">
          {{ scopeCount }}
        </small>
      </v-list-item-title>
      <v-list-item-subtitle>
        <template v-if="apiKey.description">
          {{ apiKey.description }}
        </template>
      </v-list-item-subtitle>
    </v-list-item-content>
    <v-list-item-action-text>
      <api-keys-field-hint :api-key="apiKey" />
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

    const to = fixedComputed(() => `/account/api-keys/${apiKey!.id}`)
    const scopeCount = fixedComputed(() => {
      const { scopes } = apiKey!

      if (scopes.length) {
        return `(${scopes.length} scope${scopes.length > 1 ? 's' : ''})`
      }

      return '(no scope)'
    })

    return { apiKey, onDelete, to, scopeCount }
  },
})
</script>
