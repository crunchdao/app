<template>
  <v-layout align-center justify-center>
    <v-alert v-if="errorMessage" type="error" prominent min-width="400">
      {{ errorMessage }}
    </v-alert>
    <v-alert v-else-if="connection" type="info" prominent min-width="400">
      Welcome <code>{{ connection.username }}</code
      >!
    </v-alert>
    <v-progress-circular v-else indeterminate size="48" />
  </v-layout>
</template>

<script lang="ts">
import {
  defineComponent,
  onMounted,
  ref,
  useContext,
  useRoute,
  useRouter,
  onBeforeMount,
} from '@nuxtjs/composition-api'
import { fixedComputed } from '~/composables/hack'
import { Connection } from '~/models'
import { extractMessage } from '~/utilities/error'

export default defineComponent({
  layout: 'empty',
  head: {
    title: 'OAuth Callback',
  },
  setup() {
    const { $axios } = useContext()
    const router = useRouter()
    const route = useRoute()

    const type = fixedComputed(() => route.value.params.type)
    const code = fixedComputed(() => route.value.query.code)
    const error = fixedComputed<string | null>(
      () =>
        (route.value.query.error_description ||
          route.value.query.error) as string
    )

    const errorMessage = ref<string | null>(error.value)
    const connection = ref<Connection>()

    const redirect = () => {
      router.replace('/settings/connections')
    }

    onBeforeMount(() => {
      if (!type.value) {
        redirect()
      }
    })

    onMounted(async () => {
      if (!error.value) {
        try {
          connection.value = await $axios.$post(
            `/v1/connections/${type.value}/callback`,
            {},
            {
              params: {
                code: code.value,
              },
            }
          )
        } catch (error) {
          errorMessage.value = extractMessage(error)
        }
      }

      setTimeout(redirect, 1500)
    })

    const loading = fixedComputed(
      () => !errorMessage.value && !connection.value
    )

    return {
      errorMessage,
      connection,
      loading,
    }
  },
})
</script>
