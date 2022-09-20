<template>
  <v-card>
    <card-title>Edit Model</card-title>
    <v-card-text>
      <v-alert v-if="update.errorMessage" type="error">
        {{ update.errorMessage }}
      </v-alert>
      <v-form id="form-model-update" @submit.prevent="update.submit">
        <v-text-field
          v-model="update.inputs.name"
          label="Name"
          outlined
          :error-messages="update.validations.name"
        />
        <v-textarea
          v-model="update.inputs.comment"
          label="Comment (optional)"
          outlined
          :error-messages="update.validations.comment"
          rows="3"
        />
      </v-form>
    </v-card-text>
    <v-card-actions>
      <v-btn
        color="success"
        type="submit"
        form="form-model-update"
        :loading="update.loading"
      >
        Update
      </v-btn>
      <v-btn text :to="toModel" exact> Cancel </v-btn>
      <v-spacer />
      <v-btn color="error" :loading="delete_.loading" @click="delete_.execute">
        Delete
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script lang="ts">
import {
  defineComponent,
  PropType,
  reactive,
  toRefs,
  useContext,
  useRouter,
} from '@nuxtjs/composition-api'
import { createPendingAction } from '~/composables/action'
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
    update: (_model: Model) => true,
    delete: (_model: Model) => true,
  },
  setup(props, { emit }) {
    const { $dialog, $axios } = useContext()
    const router = useRouter()

    const { model, modelId } = toRefs(props)

    const toModel = fixedComputed(() => `/performance/models/${modelId.value}`)

    const update = reactive(
      createPendingRequest<Model>({
        url: `/v1/models/${modelId.value}`,
        method: 'PATCH',
        inputs: {
          name: model.value.name,
          comment: model.value.comment,
        },
        onSuccess(model) {
          $dialog.notify.success('Model updated!')

          emit('update', model.value)

          router.push({
            path: toModel.value,
          })
        },
      })
    )

    const delete_ = reactive(
      createPendingAction(async () => {
        const response = await $dialog.confirm({
          type: 'error',
          title: 'Deletion confirmation',
          text: 'Are you sure you want to delete this model? All submission and scores will be removed',
        })

        if (response) {
          await $axios.delete(`/v1/models/${modelId.value}`)

          $dialog.notify.success('Model deleted!')
          emit('delete', model.value)

          router.push({
            path: "/performance/models/"
          })
        }
      })
    )

    return { update, delete_, toModel }
  },
})
</script>
