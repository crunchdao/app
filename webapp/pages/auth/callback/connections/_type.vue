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
  Ref,
} from '@nuxtjs/composition-api'
import { fixedComputed } from '~/composables/hack'
import { Connection } from '~/models'
import { extractMessage } from '~/utilities/error'

export default defineComponent({
  layout: 'empty',
  setup() {
    const { $axios } = useContext()
    const router = useRouter()
    const route = useRoute()

    const type = fixedComputed(() => route.value.params.type)
    const code = fixedComputed(() => route.value.query.code)

    const errorMessage = ref<string>()
    const connection = ref<Connection>()
    // const connection = ref<Connection>({
    //   userId: 'f4a62560-e335-4e85-9707-a65af42fcc2c',
    //   type: 'DISCORD',
    //   handle: 'Enzo - (っ◔◡◔)っ#4224',
    //   username: 'Enzo - (っ◔◡◔)っ',
    //   profileUrl: null,
    //   createdAt: '2022-08-20T20:59:22.7558393',
    //   updatedAt: '2022-08-20T20:59:22.7568388',
    //   public: false,
    // })

    const redirect = () => {
      router.replace('/settings/connections')
    }

    onBeforeMount(() => {
      if (!type.value) {
        redirect()
      }
    })

    onMounted(async () => {
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
