<template>
  <v-card :loading="loading">
    <template #progress>
      <v-progress-linear
        absolute
        :value="progress"
        :indeterminate="indeterminate"
      />
    </template>
    <card-title style="white-space: pre"
      >Upload <small>your work</small></card-title
    >
    <v-card-text>
      <v-alert v-if="errorMessage" type="error">
        {{ errorMessage }}
      </v-alert>
      <v-form id="form-submission-upload" @submit.prevent="submit">
        <v-file-input
          v-model="inputs.file"
          label="File"
          outlined
          :error-messages="validations.file"
        />
        <v-textarea
          v-model="inputs.comment"
          prepend-icon="mdi-comment"
          label="Comment (optional)"
          outlined
          rows="3"
          :error-messages="validations.comment"
        />
      </v-form>
    </v-card-text>
    <v-card-actions>
      <v-btn
        color="success"
        type="submit"
        form="form-submission-upload"
        :loading="loading"
      >
        Upload
      </v-btn>
      <v-btn text :disabled="loading" @click="resetAll"> Reset </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script lang="ts">
import {
  defineComponent,
  PropType,
  ref,
  toRefs,
  unref,
  useContext,
} from '@nuxtjs/composition-api'
import { fixedComputed } from '~/composables/hack'
import { createPendingRequest } from '~/composables/request'
import { Model, Submission } from '~/models'

export default defineComponent({
  head: {
    title: 'Models',
  },
  props: {
    model: {
      type: Object as PropType<Model>,
      required: true,
    },
  },
  emits: {
    create: (submission_: Submission) => true,
  },
  setup(props, { emit }) {
    const { model } = toRefs(props)

    const { $dialog } = useContext()

    const progress = ref(50)

    const create = createPendingRequest<Submission>({
      url: '/v1/submissions',
      method: 'POST',
      formData: true,
      inputs: {
        file: null as File | null,
        comment: '',
        modelId: model.value.id,
      },
      onSuccess(model) {
        emit('create', unref(model))

        $dialog.notify.success(`Submission created!`)

        create.resetInputs()
      },
      onUploadProgress(event: ProgressEvent) {
        progress.value = (event.loaded / event.total) * 100
      },
    })

    const indeterminate = fixedComputed(() => {
      return progress.value == 0 || progress.value == 100
    })

    return { ...create, progress, indeterminate }
  },
})
</script>
