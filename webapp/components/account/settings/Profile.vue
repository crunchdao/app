<template>
  <v-card>
    <card-title>
      Profile Information
      <template #action>
        <v-btn light>
          <v-icon left>mdi-pencil</v-icon>
          edit
        </v-btn>
      </template>
    </card-title>
    <v-card-text>
      <v-flex class="d-flex">
        <v-flex shrink>
          <avatar-editor />
        </v-flex>
        <v-flex grow>
          <v-skeleton-loader
            boilerplate
            type="table-heading, list-item-two-line, table-tfoot"
          />
        </v-flex>
      </v-flex>
    </v-card-text>
  </v-card>
</template>
