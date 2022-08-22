<template>
  <client-only>
    <!-- SSR does not seems to work with the component -->
    <v-radio-group
      label="Scopes"
      :error-messages="errorMessages"
      class="pt-0 mt-0"
    >
      <v-checkbox
        v-for="scope in scopes"
        :key="scope.name"
        v-model="model"
        :value="scope.name"
        :label="scope.name"
        :hint="scope.description"
        multiple
        dense
        class="my-1 mt-0 mb-1"
        persistent-hint
      />
    </v-radio-group>
  </client-only>
</template>

<script lang="ts">
import {
  computed,
  defineComponent,
  PropType,
  ref,
  useContext,
  useFetch,
  watch,
} from '@nuxtjs/composition-api'
import { Scope } from '~/models'
import isEqual from 'lodash.isequal'

export default defineComponent({
  layout: 'settings',
  props: {
    errorMessages: {
      type: [String, Array] as PropType<string | Array<string>>,
    },
    value: {
      type: Array as PropType<Array<string>>,
      required: true,
    },
  },
  emits: {
    input: (_value: Array<string>) => true,
  },
  setup(props, { emit }) {
    const { $axios } = useContext()

    const scopes = ref<Array<Scope>>([])
    useFetch(async () => {
      scopes.value = await $axios.$get('/v1/api-keys/scopes')
    })

    const model = computed<Array<string>>({
      get() {
        return props.value
      },
      set(value) {
        emit('input', value)
      },
    })

    return {
      errorMessages: props.errorMessages,
      scopes,
      model,
    }
  },
})
</script>
