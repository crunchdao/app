<template>
  <div>
    <v-card-title>
      Edit API Keys
      <v-spacer />
      <v-btn style="visibility: hidden" />
    </v-card-title>
    <v-divider />
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
        <v-btn color="success" type="submit" form="form-api-key-create">
          Update
        </v-btn>
        <v-btn text to="/settings/api-keys" exact> Cancel </v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  useContext,
  useFetch,
  useRoute,
  useRouter,
  watch,
} from '@nuxtjs/composition-api'
import { fixedComputed } from '~/composables/hack'
import { createPendingRequest } from '~/composables/request'
import { ApiKey, Scope } from '~/models'
import { extractMessage } from '~/utilities/error'

export default defineComponent({
  layout: 'settings',
  setup() {
    const { $axios, $dialog } = useContext()
    const router = useRouter()
    const route = useRoute()

    const id = route.value.params.id

    const update = createPendingRequest<ApiKey>({
      url: `/v1/api-keys/${id}`,
      method: 'PATCH',
      inputs: {
        name: null,
        description: null,
        scopes: [],
      },
      onSuccess() {
        $dialog.notify.success('API Key updated!')

        router.push({
          path: '/settings/api-keys',
        })
      },
    })

    const { fetchState } = useFetch(async () => {
      const apiKey = await $axios.$get<ApiKey>(`/v1/api-keys/${id}`)

      update.updateInputs(apiKey)
    })

    const scopes = ref<Array<Scope>>([])
    useFetch(async () => {
      scopes.value = await $axios.$get('/v1/api-keys/scopes')
    })

    const loading = fixedComputed(
      () => update.loading.value || fetchState.pending
    )

    const errorMessage = fixedComputed(
      () => update.errorMessage.value || extractMessage(fetchState.error)
    )

    return { ...update, scopes, loading, errorMessage }
  },
})
</script>
