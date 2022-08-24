<template>
  <div>
    <settings-title title="Create an API Keys" />
    <v-card-subtitle>
      API Keys can be used to authenticate to the CrunchDAO API.
    </v-card-subtitle>
    <v-alert v-if="errorMessage" type="error">
      {{ errorMessage }}
    </v-alert>
    <v-card outlined>
      <v-card-text>
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
          <api-keys-field-scopes
            v-model="inputs.scopes"
            :error-messages="validations.scopes"
          />
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-btn color="success" type="submit" form="form-api-key-create">
          Create
        </v-btn>
        <v-btn text to="/settings/api-keys" exact> Cancel </v-btn>
      </v-card-actions>
    </v-card>
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
          Make sure to copy your API Key now. You won’t be able to see it again!
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
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  useContext,
  useRouter,
  watch,
} from '@nuxtjs/composition-api'
import { fixedComputed } from '~/composables/hack'
import copyToClipboard from 'copy-to-clipboard'
import { createPendingRequest } from '~/composables/request'
import { ApiKey } from '~/models'

export default defineComponent({
  layout: 'settings',
  emits: {
    create: (_apiKey: ApiKey) => true,
  },
  setup(_, { emit }) {
    const { $dialog } = useContext()
    const router = useRouter()

    const dialog = ref(false)

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

    const loading = fixedComputed(() => create.loading.value)

    watch(dialog, (dialog) => {
      if (!dialog) {
        create.resetAll()
      }
    })

    const onClose = () => {
      create.resetAll()
      dialog.value = false

      router.push({
        path: '/settings/api-keys',
      })
    }

    const onCopy = () => {
      copyToClipboard(create.value.value!.plain!)

      $dialog.notify.success(`Copied!`)
    }

    return { dialog, ...create, loading, onClose, onCopy }
  },
})
</script>
