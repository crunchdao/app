<template>
  <v-list-item @click="onClick">
    <v-list-item-avatar>
      <v-icon>mdi-content-copy</v-icon>
    </v-list-item-avatar>
    <v-list-item-content>
      <v-list-item-title> Copy ID </v-list-item-title>
    </v-list-item-content>
  </v-list-item>
</template>

<script lang="ts">
import { defineComponent, useContext } from '@nuxtjs/composition-api'
import copyToClipboard from 'copy-to-clipboard'
import { ApiKey } from '@/models'

export default defineComponent({
  props: {
    apiKey: {
      required: true,
    } as Vue.PropOptions<ApiKey>,
  },
  setup(props) {
    const { $dialog } = useContext()

    const { apiKey } = props

    const onClick = () => {
      copyToClipboard(apiKey!.id)

      $dialog.notify.success(`Copied!`)
    }

    return { apiKey, onClick }
  },
})
</script>
