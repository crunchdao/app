<template>
  <v-dialog v-model="dialog" width="500" scrollable>
    <template #activator="{ on, attrs }">
      <slot name="activator" :on="on" :attrs="attrs" />
    </template>
    <v-card :loading="loading">
      <v-card-title class="text-h5"> Create an API Keys </v-card-title>
      <v-card-text class="py-2">
        <v-alert v-if="errorMessage" type="error">
          {{ errorMessage }}
        </v-alert>
        <v-form id="form-api-key-create" @submit.prevent="submit">
          <v-text-field
            v-model="inputs.name"
            label="Name"
            outlined
            :error-messages="validations.name"
          />
          <v-textarea
            v-model="inputs.description"
            label="Description (optional)"
            outlined
            :error-messages="validations.description"
            rows="3"
          />
          <v-radio-group
            label="Scopes"
            :error-messages="validations.scopes"
            class="pt-0 mt-0"
          >
            <v-checkbox
              v-for="scope in scopes"
              :key="scope.name"
              :value="scope.name"
              :label="scope.name"
              dense
              v-model="inputs.scopes"
              hide-details
              class="my-1"
            />
          </v-radio-group>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-btn
          large
          color="primary"
          block
          type="submit"
          form="form-api-key-create"
        >
          Create
        </v-btn>
      </v-card-actions>
      <v-dialog
        v-if="value"
        :value="true"
        width="650"
        scrollable
        @click:outside="onClose"
      >
        <v-card>
          <v-card-title class="text-h5"> {{ value.name }} </v-card-title>
          <v-card-subtitle>
            Make sure to copy your API Key now. You won’t be able to see it
            again!
          </v-card-subtitle>
          <v-card-text style="font-family: monospace">
            <v-text-field
              :value="value.plain"
              readonly
              outlined
              hide-details
              append-icon="mdi-content-copy"
              @click:append="onCopy"
            />
          </v-card-text>
          <v-card-actions>
            <v-btn large color="primary" block @click="onClose"> Close </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  useContext,
  useFetch,
  watch,
} from '@nuxtjs/composition-api'
import { fixedComputed } from '~/composables/hack'
import copyToClipboard from 'copy-to-clipboard'
import { createPendingRequest } from '~/composables/request'
import { ApiKey, Scope } from '~/models'

export default defineComponent({
  emits: {
    create: (_apiKey: ApiKey) => true,
  },
  setup(_, { emit }) {
    const { $axios, $dialog } = useContext()

    const dialog = ref(false)

    const scopes = ref([] as Array<Scope>)
    const { fetch, fetchState } = useFetch(async () => {
      scopes.value = await $axios.$get('/v1/api-keys/scopes')
    })

    const create = createPendingRequest<ApiKey>({
      url: '/v1/api-keys',
      method: 'PUT',
      inputs: {
        name: null,
        description: null,
        scopes: [],
      },
      onSuccess(apiKey) {
        const copy = { ...apiKey.value }
        delete copy['plain']

        emit('create', copy)
      },
    })

    const loading = fixedComputed(
      () => create.loading.value || fetchState.pending
    )

    watch(dialog, (dialog) => {
      if (dialog) {
        fetch()
      } else {
        create.resetAll()
      }
    })

    const onClose = () => {
      create.resetAll()
      dialog.value = false
    }

    const onCopy = () => {
      copyToClipboard(create.value.value!.plain!)

      $dialog.notify.success(`Copied!`)
    }

    return { dialog, ...create, scopes, loading, onClose, onCopy }
  },
})
</script>

<style scoped>
.centered-input ::v-deep(input) {
  text-align: center;
}
</style>