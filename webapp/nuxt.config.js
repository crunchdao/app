import colors from 'vuetify/es5/util/colors'

export default {
  // Global page headers: https://go.nuxtjs.dev/config-head
  head: {
    titleTemplate: '%s - CrunchDAO App',
    title: 'CrunchDAO App',
    htmlAttrs: {
      lang: 'en',
    },
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' },
      { name: 'format-detection', content: 'telephone=no' },
    ],
    link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }],
  },

  // Global CSS: https://go.nuxtjs.dev/config-css
  css: [],

  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: [],

  // Auto import components: https://go.nuxtjs.dev/config-components
  components: true,

  publicRuntimeConfig: {
    axios: {
      browserBaseURL: "/api/"
    }
  },

  privateRuntimeConfig: {
    axios: {
      // TODO Change
      baseURL: "http://localhost:8000/"
    }
  },

  env: {
    // https://developers.google.com/recaptcha/docs/faq
    recaptchaSiteKey: process.env.RECAPTCHA_SITE_KEY || '6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI'
  },

  server: {
    host: "0.0.0.0"
  },

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [
    // https://go.nuxtjs.dev/typescript
    '@nuxt/typescript-build',
    '@nuxtjs/composition-api/module',
    ['@pinia/nuxt', { disableVuex: false }],
    // https://go.nuxtjs.dev/vuetify
    '@nuxtjs/vuetify',
  ],

  // Modules: https://go.nuxtjs.dev/config-modules
  modules: [
    // https://go.nuxtjs.dev/axios
    '@nuxtjs/axios',
    '@nuxtjs/auth-next',
    '@nuxtjs/proxy',
    'vuetify-dialog/nuxt',
  ],

  // Axios module configuration: https://go.nuxtjs.dev/config-axios
  axios: {
    // Workaround to avoid enforcing hard-coded localhost:3000: https://github.com/nuxt-community/axios-module/issues/308
    baseURL: '/',
  },

  // Vuetify module configuration: https://go.nuxtjs.dev/config-vuetify
  vuetify: {
    customVariables: ['~/assets/variables.scss'],
    theme: {
      dark: true,
      themes: {
        dark: {
          primary: '#f76a49',
          accent: '#ff4081',
          secondary: colors.amber.darken3,
          info: colors.teal.lighten1,
          warning: colors.amber.base,
          error: colors.deepOrange.accent4,
          success: colors.green.accent3,
        },
        light: {
          primary: '#f76a49',
          accent: '#ff4081',
          secondary: colors.amber.lighten3,
          info: colors.teal.lighten1,
          warning: colors.amber.base,
          error: colors.deepOrange.accent4,
          success: colors.green.accent3,
        },
      },
    },
  },

  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {
    transpile: ['claygl', 'echarts', 'zrender']
  },

  auth: {
    redirect: {
      login: '/',
      logout: '/',
      callback: '/auth/callback/keycloak',
    },
    localStorage: false,
    sessionStorage: false,
    defaultStrategy: "keycloak",
    strategies: {
      local: false,
      keycloak: {
        name: "keycloak",
        scheme: "~/schemes/custom.js",
        enabled: true,
        endpoints: {
          // TODO Change
          configuration: 'http://localhost:9800/realms/App/.well-known/openid-configuration',
          userInfo: '/v1/users/@self'
        },
        token: {
          property: "access_token",
          type: "Bearer",
          name: "Authorization",
          maxAge: 300,
        },
        refreshToken: {
          property: "refresh_token",
          maxAge: 60 * 60 * 24 * 30,
        },
        user: {
          autoFetch: true
        },
        idToken: {
          property: "id_token"
        },
        responseType: "code",
        grantType: "authorization_code",
        clientId: "frontend",
        scope: ["openid", "profile", "email"],
        codeChallengeMethod: "S256",
        acrValues: "0",
      },
    },
    watchLoggedIn: true,
    rewriteRedirects: true,
  },

  proxy: {
    '/api/': {
      changeOrigin: false,
      pathRewrite: { '^/api/': '/' },
      target: 'http://localhost:8000/',
    },
  },
}
