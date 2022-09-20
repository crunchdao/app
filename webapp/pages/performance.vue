<template>
  <v-container>
    <v-toolbar rounded class="mb-6">
      <v-tabs>
        <v-tab to="/performance/profile"> Profile </v-tab>
        <v-tab :to="myModelTabUrl">
          My Models
          <v-select
            v-model="selectedModelId"
            dense
            hide-details
            solo
            :items="models"
            item-value="id"
            item-text="name"
            height="48"
            style="width: 220px; height: 48px"
            class="ml-2"
            background-color="transparent"
            clearable
            flat
            label="Select a model"
          />
        </v-tab>
      </v-tabs>
    </v-toolbar>
    <nuxt-child @create="fetch" @update="fetch" @delete="fetch" />
  </v-container>
</template>

<script lang="ts">
import {
  computed,
  defineComponent,
  ref,
  useContext,
  useFetch,
  useRoute,
  useRouter,
} from '@nuxtjs/composition-api'
import { useAuth } from '~/composables/auth'
import { fixedComputed } from '~/composables/hack'
import { Model, UUID } from '~/models'

export default defineComponent({
  head: {
    title: 'Performance',
    titleTemplate: '%s - Performance',
  },
  setup() {
    const { $axios } = useContext()
    const router = useRouter()
    const route = useRoute()
    const { user } = useAuth()

    const models = ref<Array<Model>>([])

    const { fetch, fetchState } = useFetch(async () => {
      models.value = await $axios.$get(`/v1/models`, {
        params: {
          userId: user.value?.id,
        },
      })
    })

    const selectedModelId = computed<UUID>({
      get() {
        return route.value.params.id
      },
      set(id: UUID | undefined) {
        if (id) {
          router.push({
            path: `/performance/models/${id}`,
          })
        } else if (route.value.path.startsWith('/performance/models/')) {
          router.push({
            path: `/performance/models`,
          })
        }
      },
    })

    const myModelTabUrl = fixedComputed(() => {
      if (!selectedModelId.value) {
        return `/performance/models`
      }

      return `/performance/models/${selectedModelId.value}`
    })

    return {
      models,
      fetch,
      fetchState,
      selectedModelId,
      myModelTabUrl,
    }
  },
})
</script>
