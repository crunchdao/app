<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <v-card>
          <v-card-title> Account </v-card-title>
          <v-card-text>
            <pre>{{ user }}</pre>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12">
        <v-card>
          <v-card-title> Achievements </v-card-title>
          <v-card-text>
            <pre>{{ achievementUsers }}</pre>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  useContext,
  useFetch,
} from '@nuxtjs/composition-api'
import { Achievement, AchievementUser } from '~/models'

export default defineComponent({
  head: {
    title: 'Account',
  },
  setup() {
    const { $axios, $auth } = useContext()
    const { user } = useContext().$auth

    const achievementUsers = ref<Array<AchievementUser>>([])
    useFetch(async () => {
      achievementUsers.value = (await $axios.$post('/graphql', {
        query: `
          query AchievementUser($userId: UUID!) {
            achievementUser(userId: $userId) {
              achievement {
                id
                name
                description
                percentage
                max
              }
              progress
              unlocked
              unlockedAt
            }
          }
        `,
        variables: {
          userId: $auth.user!.id,
        },
      })).data?.achievementUser || []
    })

    return { user, achievementUsers }
  },
})
</script>
