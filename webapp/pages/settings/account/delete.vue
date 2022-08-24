<template>
  <div>
    <settings-title title="Delete account" class="error--text" />
    <v-card-subtitle>This action cannot be undone.</v-card-subtitle>
    <v-card-text>
      <table class="table">
        <tr>
          <th>What will be deleted</th>
          <th>What will <b>not</b> be deleted</th>
        </tr>
        <tr>
          <td class="pr-4">
            <ul>
              <li>
                Personal informations
                <ul>
                  <li>Email</li>
                  <li>Name <small>(first/last/username)</small></li>
                </ul>
              </li>
              <li>Connections</li>
              <li>Notifications</li>
              <li>API Keys</li>
              <li>Achievements</li>
            </ul>
          </td>
          <td class="pr-4">
            <ul>
              <li>Models</li>
              <li>Leaderboard Positions</li>
            </ul>
          </td>
        </tr>
      </table>
    </v-card-text>
    <v-card-text>
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
      <v-btn
        :loading="loading"
        :disabled="disabled"
        color="error"
        large
        @click="onClick"
      >
        Delete your account
      </v-btn>
    </v-card-text>
  </div>
</template>

<script lang="ts">
import VueRecaptcha from 'vue-recaptcha'
import {
  defineComponent,
  ref,
  useContext,
  useRouter,
} from '@nuxtjs/composition-api'
import { fixedComputed } from '~/composables/hack'
import { createPendingAction } from '~/composables/action'

export default defineComponent({
  components: { VueRecaptcha },
  layout: 'settings',
  head: {
    title: 'Account Deletion',
  },
  setup() {
    const { $dialog, $axios, $auth } = useContext()
    const router = useRouter()

    const captcha = ref<any>(null)
    const recaptchaResponse = ref<string>()

    const { loading, execute: deleteAccount } = createPendingAction(
      async () => {
        await $axios.$delete(`/v1/registration`, {
          params: {
            recaptchaResponse: recaptchaResponse.value,
          },
        })

        $dialog.notify.success(`Account deleted!`)
        router.push({
          path: `/auth/logout`,
        })
      }
    )

    const recaptcha = {
      siteKey: process.env.recaptchaSiteKey,
      onVerify(response: string) {
        recaptchaResponse.value = response
      },
      onExpired() {
        recaptchaResponse.value = undefined
      },
      onRenderMethod() {
        recaptchaResponse.value = undefined
      },
      onRenderError(error: Error | any) {
        recaptchaResponse.value = undefined
        $dialog.notify.error(`Recaptcha: ${error}`)
      },
    }

    const resetCaptcha = () => {
      recaptchaResponse.value = undefined
      captcha.value!.reset()
    }

    const disabled = fixedComputed(() => !recaptchaResponse.value)

    const onClick = async () => {
      const response = await $dialog.confirm({
        title: 'Account deletion confirmation',
        text: 'This action cannot be undone!',
        type: 'error',
      })

      if (response) {
        deleteAccount()
      } else {
        resetCaptcha()
      }
    }

    return {
      captcha,
      recaptcha,
      disabled,
      loading,
      onClick,
    }
  },
})
</script>

<style scoped>
.table * {
  text-align: left;
  vertical-align: top;
}
</style>
