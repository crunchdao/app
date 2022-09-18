<template>
  <v-container>
    <v-card>
      <card-title>
        <v-btn icon to="/admin/users" exact-path class="mr-2">
          <v-icon>mdi-arrow-left</v-icon>
        </v-btn>
        {{ user.username }}
      </card-title>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import {
  defineComponent,
  PropType,
  toRefs,
  useMeta,
} from '@nuxtjs/composition-api'
import { User } from '~/models'

export default defineComponent({
  head: {
    title: 'User',
  },
  props: {
    user: {
      type: Object as PropType<User>,
      required: true,
    },
  },
  setup(props) {
    const { user } = toRefs(props)

    useMeta(() => ({
      title: user.value.username,
    }))

    return {
      user,
    }
  },
})
</script>
