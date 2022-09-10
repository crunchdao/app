<template>
  <v-row>
    <v-col cols="12">
      <account-settings-profile />
    </v-col>
    <v-col id="connections" cols="12">
      <client-only>
        <account-settings-connections />
      </client-only>
    </v-col>
    <v-col cols="12">
      <account-settings-delete />
    </v-col>
  </v-row>
</template>
