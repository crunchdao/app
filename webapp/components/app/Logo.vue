<template>
  <v-flex class="d-flex flex-row align-center ma-4">
    <router-link to="/">
      <v-img
        :src="require('@/assets/logo.png')"
        max-height="48"
        max-width="48"
      />
    </router-link>
    <span v-if="!props.imageOnly" class="ml-2" style="line-height: 1">
      crunch <br />
      dao
    </span>
  </v-flex>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  props: {
    imageOnly: Boolean,
  },
  setup(props) {
    return {
      props,
    }
  },
})
</script>

