<template>
  <v-card>
    <card-title> Create a Model </card-title>
    <v-card-text>
    <v-alert v-if="errorMessage" type="error">
      {{ errorMessage }}
    </v-alert>
      <v-form id="form-model-create" @submit.prevent="submit">
        <v-text-field
          v-model="inputs.name"
          label="Name"
          outlined
          :error-messages="validations.name"
        />
        <v-textarea
          v-model="inputs.comment"
          label="Comment (optional)"
          outlined
          :error-messages="validations.comment"
          rows="3"
        />
      </v-form>
    </v-card-text>
    <v-card-actions>
      <v-btn color="success" type="submit" form="form-model-create">
        Create
      </v-btn>
      <v-btn text to="/performance/models" exact> Cancel </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script lang="ts">
import {
  defineComponent,
  unref,
  useContext,
  useRouter,
} from '@nuxtjs/composition-api'
import { createPendingRequest } from '~/composables/request'
import { Model } from '~/models'

export default defineComponent({
  head: {
    title: 'Create Model',
  },
  emits: {
    create: (_model: Model) => true,
  },
  setup(_, { emit }) {
    const { $dialog } = useContext()
    const router = useRouter()

    const create = createPendingRequest<Model>({
      url: '/v1/models',
      method: 'PUT',
      inputs: {
        name: "",
        comment: "",
      },
      onSuccess(model) {
        emit('create', unref(model))

        $dialog.notify.success(`Model created!`)

        router.push({
          path: `/performance/models/${model.value.id}`,
        })
      },
    })

    return { ...create }
  },
})
</script>
