<template>
  <v-container>
    <v-card class="mb-4">
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
    </v-card>
    <nuxt-child @update="onUpdate" />
  </v-container>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  useContext,
  useFetch,
  useRouter,
  watch,
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
    const { user } = useAuth()

    const models = ref<Array<Model>>([])

    const { fetch, fetchState } = useFetch(async () => {
      models.value = await $axios.$get(`/v1/models`, {
        params: {
          userId: user.value?.id,
        },
      })
    })

    const selectedModelId = ref<UUID>()
    watch(selectedModelId, (id) => {
      if (id) {
        router.push({
          path: `/performance/models/${id}`,
        })
      } else {
        router.push({
          path: `/performance/models`,
        })
      }
    })

    const myModelTabUrl = fixedComputed(() => {
      if (!selectedModelId.value) {
        return `/performance/models`
      }

      return `/performance/models/${selectedModelId.value}`
    })

    const onUpdate = (model: Model) => {
      fetch()
    }

    return {
      models,
      fetchState,
      selectedModelId,
      myModelTabUrl,
      onUpdate,
    }
  },
})
</script>
