<template>
  <v-main class="background">
    <v-row class="fill-height" align="center" justify="center">
      <v-col align="center">
        <v-card width="600">
          <v-card-title class="text-h2">
            <v-spacer />
            WELCOME
            <v-spacer />
          </v-card-title>
          <v-card-subtitle class="text-center"> Registration </v-card-subtitle>
          <v-card-text>
            <v-form>
              <v-text-field label="First name" outlined />
              <v-text-field label="Last name" outlined />
              <v-text-field label="Email" outlined type="email" />
              <v-text-field label="Password" outlined type="password" />
              <v-btn large color="primary" block> Register </v-btn>
            </v-form>
            <v-card-subtitle class="text-center">
              You already have an account?
              <router-link to="/auth/login">Log-in</router-link>
            </v-card-subtitle>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col sm="0" md="6" />
    </v-row>
  </v-main>
</template>

<script lang="ts">
import { defineComponent } from '@nuxtjs/composition-api'

export default defineComponent({
  layout: 'empty',
  setup() {},
})
</script>

<style scoped>
.background {
  background-image: url('https://r4.wallpaperflare.com/wallpaper/727/654/382/background-blemishes-dark-wallpaper-58060c0a0f1cc7397e20c97202a95bc0.jpg');
  background-position: center;
}
</style>