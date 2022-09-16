<template>
  <v-card>
    <card-title>Edit Model</card-title>
    <v-card-text>
      <v-alert v-if="errorMessage" type="error">
        {{ errorMessage }}
      </v-alert>
      <v-form id="form-model-update" @submit.prevent="submit">
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
      <v-btn color="success" type="submit" form="form-model-update" :loading="loading">
        Update
      </v-btn>
      <v-btn text :to="toModel" exact> Cancel </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script lang="ts">
import {
  defineComponent,
  PropType,
  toRefs,
  useContext,
  useRouter,
} from '@nuxtjs/composition-api'
import { fixedComputed } from '~/composables/hack'
import { createPendingRequest } from '~/composables/request'
import { Model, UUID } from '~/models'

export default defineComponent({
  head: {
    title: 'Edit Model',
  },
  props: {
    model: {
      type: Object as PropType<Model>,
      required: true,
    },
    modelId: {
      type: String as PropType<UUID>,
      required: true,
    },
  },
  emits: {
    update: (_model: Model) => true
  },
  setup(props, { emit }) {
    const { $dialog } = useContext()
    const router = useRouter()

    const { model, modelId } = toRefs(props)

    const toModel = fixedComputed(() => `/performance/models/${modelId.value}`)

    const update = createPendingRequest<Model>({
      url: `/v1/models/${modelId.value}`,
      method: 'PATCH',
      inputs: {
        name: model.value.name,
        comment: model.value.comment,
      },
      onSuccess(model) {
        $dialog.notify.success('Model updated!')

        emit("update", model.value)

        router.push({
          path: toModel.value,
        })
      },
    })

    return { ...update, toModel }
  },
})
</script>
