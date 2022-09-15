<template>
  <v-main class="background">
    <v-row class="fill-height" align="center" justify="center">
      <v-col align="center">
        <v-card width="600" class="ma-4">
          <v-card-title class="text-h2">
            <v-spacer />
            WELCOME
            <v-spacer />
          </v-card-title>
          <template v-if="user">
            <v-card-title class="text-h3 pt-0">
              <v-spacer />
              {{ user.firstName }}
              <v-spacer />
            </v-card-title>
            <v-card-text v-if="user">
              <v-alert type="success"> You will be redirected soon! </v-alert>
            </v-card-text>
          </template>
          <template v-else>
            <v-card-subtitle class="text-center">
              Registration
            </v-card-subtitle>
            <v-card-text>
              <v-alert v-if="errorMessage" type="error">
                {{ errorMessage }}
              </v-alert>
              <v-form @submit.prevent="submit">
                <v-text-field
                  v-model="inputs.firstName"
                  label="First name"
                  outlined
                  :error-messages="validation.firstName"
                />
                <v-text-field
                  v-model="inputs.lastName"
                  label="Last name"
                  outlined
                  :error-messages="validation.lastName"
                />
                <v-text-field
                  v-model="inputs.username"
                  label="Username"
                  outlined
                  :error-messages="validation.username"
                />
                <v-text-field
                  v-model="inputs.email"
                  label="Email"
                  outlined
                  type="email"
                  :error-messages="validation.email"
                />
                <v-text-field
                  v-model="inputs.password"
                  label="Password"
                  outlined
                  type="password"
                  :error-messages="validation.password"
                />
                <v-text-field
                  v-model="inputs.confirmPassword"
                  label="Password Confirm"
                  outlined
                  type="password"
                  :error-messages="validation.confirmPassword"
                />
                <client-only>
                  <vue-recaptcha
                    class="mb-4"
                    ref="captcha"
                    :loadRecaptchaScript="true"
                    :sitekey="recaptcha.siteKey"
                    @verify="recaptcha.onVerify"
                    @expired="recaptcha.onExpired"
                    @render="recaptcha.onRenderMethod"
                    @error="recaptcha.onRenderError"
                  />
                </client-only>
                <v-btn large color="primary" block type="submit">
                  Register
                </v-btn>
              </v-form>
              <v-card-subtitle class="text-center">
                You already have an account?
                <router-link to="/auth/login">Log-in</router-link>
              </v-card-subtitle>
            </v-card-text>
          </template>
        </v-card>
      </v-col>
      <v-col sm="0" md="6" />
    </v-row>
  </v-main>
</template>

<script lang="ts">
import VueRecaptcha from 'vue-recaptcha'
import {
  computed,
  defineComponent,
  reactive,
  ref,
  Ref,
  useContext,
  useRoute,
  useRouter,
  watch,
} from '@nuxtjs/composition-api'
import { ApiError, User } from '~/models'

export default defineComponent({
  components: { VueRecaptcha },
  layout: 'empty',
  head: {
    title: 'Register',
  },
  setup() {
    const { $axios, $dialog } = useContext()
    const router = useRouter()

    const user = ref<User>()
    const captcha = ref<any>()

    const inputs = reactive({
      firstName: null as string | null,
      lastName: null as string | null,
      username: null as string | null,
      email: null as string | null,
      password: null as string | null,
      confirmPassword: null as string | null,
      recaptchaResponse: null as string | null,
      referralCode: null as string | null,
    })

    const route = useRoute()
    const referralCode = computed(() => route.value.query.referral)
    watch(
      referralCode,
      (value) => {
        inputs.referralCode = value as string
      },
      { immediate: true }
    )

    const errorMessage = ref() as Ref<string | null>
    const validation = reactive({
      firstName: [],
      lastName: [],
      username: [],
      email: [],
      password: [],
      confirmPassword: [],
    }) as { [key: string]: Array<string> }

    const loading = ref(false)

    const redirect = () => {
      setTimeout(() => {
        router.replace({
          path: `/auth/login`,
          query: {
            username: user.value!.username,
          },
        })
      }, 2000)
    }

    const submit = async () => {
      try {
        errorMessage.value = null
        loading.value = true
        for (const key in validation) {
          validation[key] = []
        }

        user.value = await $axios.$post('/v1/registration', inputs)

        redirect()
      } catch (error: Error | any) {
        const response = error?.response
        if (response) {
          const { data } = response as { data: ApiError }
          errorMessage.value = data?.message || 'An error occured'

          if (response.status == 400) {
            for (const { property, message } of data.fieldErrors || []) {
              validation[property].push(message)
            }
          }
        } else {
          errorMessage.value =
            error?.message || String(error) || 'An error occured'
        }

        resetCaptcha()
      } finally {
        loading.value = false
      }
    }

    const recaptcha = {
      siteKey: process.env.recaptchaSiteKey,
      onVerify(response: string) {
        inputs.recaptchaResponse = response
      },
      onExpired() {
        inputs.recaptchaResponse = null
      },
      onRenderMethod() {
        inputs.recaptchaResponse = null
      },
      onRenderError(error: Error | any) {
        inputs.recaptchaResponse = null
        $dialog.notify.error(`Recaptcha: ${error}`)
      },
    }

    const resetCaptcha = () => {
      captcha.value!.reset()
    }

    return {
      user,
      inputs,
      errorMessage,
      validation,
      loading,
      captcha,
      submit,
      recaptcha,
    }
  },
})
</script>

<style scoped>
.background {
  background-image: url('https://r4.wallpaperflare.com/wallpaper/727/654/382/background-blemishes-dark-wallpaper-58060c0a0f1cc7397e20c97202a95bc0.jpg');
  background-position: center;
}
</style>