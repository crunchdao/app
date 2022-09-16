<template>
  <v-layout v-if="!model" align-center justify-center fill-height>
    <v-progress-circular v-if="$fetchState.pending" indeterminate />
    <v-flex v-else style="max-width: 400px">
      <v-alert type="error">
        Could not load the page: <br />
        <code>{{ error }}</code>
      </v-alert>
      <v-btn block @click="fetch">
        <v-icon left>mdi-refresh</v-icon>
        retry
      </v-btn>
    </v-flex>
  </v-layout>
  <div v-else>
    <nuxt-child :model="model" :model-id="modelId" @update="onUpdate" />
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  useContext,
  useFetch,
  useRoute,
  watch,
} from '@nuxtjs/composition-api'
import { fixedComputed } from '~/composables/hack'
import { Model } from '~/models'
import { extractMessage } from '~/utilities/error'

export default defineComponent({
  head: {},
  emits: {
    update: (_model: Model) => true
  },
  setup(_, { emit }) {
    const { $axios } = useContext()

    const route = useRoute()
    const modelId = fixedComputed(() => route.value.params.id)

    const model = ref<Model>()

    const { fetch, fetchState } = useFetch(async () => {
      model.value = await $axios.$get(`/v1/models/${modelId.value}`)
    })

    const error = fixedComputed(() => extractMessage(fetchState.error))

    watch(modelId, () => {
      model.value = undefined
      fetch()
    })

    const onUpdate = (model_: Model) => {
      model.value = model_

      emit("update", model_)
    }

    return {
      modelId,
      model,
      fetch,
      fetchState,
      error,
      onUpdate,
    }
  },
})
</script>
