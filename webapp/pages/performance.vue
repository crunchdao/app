<template>
  <v-container>
    <v-card class="mb-4">
      <v-tabs>
        <v-tab to="/performance/profile"> Profile </v-tab>
        <v-tab :to="myModelTabUrl">
          My Models
          <v-select
            v-model="selectedModel"
            dense
            hide-details
            solo
            :items="models"
            item-text="name"
            return-object
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
    <nuxt-child />
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
import { Model } from '~/models'

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

    const { fetchState } = useFetch(async () => {
      models.value = await $axios.$get(`/v1/models`, {
        params: {
          userId: user.value?.id,
        },
      })
    })

    const selectedModel = ref<Model | null>(null)
    watch(selectedModel, (model) => {
      if (model) {
        router.push({
          path: `/performance/models/${model.id}`,
        })
      } else {
        router.push({
          path: `/performance/models`,
        })
      }
    })

    const myModelTabUrl = fixedComputed(() => {
      if (!selectedModel.value) {
        return `/performance/models`
      }

      return `/performance/models/${selectedModel.value.id}`
    })

    return {
      models,
      fetchState,
      selectedModel,
      myModelTabUrl,
    }
  },
})
</script>
