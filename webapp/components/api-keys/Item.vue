<template>
  <client-only>
    <v-list-item>
      <v-list-item-icon>
        <v-icon>mdi-key</v-icon>
      </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>
          ID: <code>{{ apiKey.id }}</code>
        </v-list-item-title>
        <v-list-item-subtitle>
          <v-chip v-for="scope in apiKey.scopes" :key="scope" x-small>
            {{ scope }}
          </v-chip>
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
        <v-menu offset-x right offset-y>
          <template #activator="{ on, attrs }">
            <v-btn icon v-bind="attrs" v-on="on">
              <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>
          </template>
          <v-list>
            <api-keys-button-delete :api-key="apiKey" @delete="onDelete" />
          </v-list>
        </v-menu>
      </v-list-item-action>
    </v-list-item>
  </client-only>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'
import { ApiKey } from '@/models'

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

    const onDelete = () => emit('delete')

    return { apiKey }
  },
})
</script>
