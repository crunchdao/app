<template>
  <v-container>
    <v-card>
      <v-card-text>
        <v-flex class="d-flex">
          <v-flex shrink>
            <avatar :user-id="user.id" size="128" />
          </v-flex>
          <v-flex grow class="ml-4">
            <div class="text-h3">{{ user.username }}</div>
            <div v-if="followStatistics" class="subtitle-1 text-uppercase">
              <router-link :to="toFollowers">
                {{ followStatistics.followers }} follower(s)
              </router-link>
              <br />
              <router-link :to="toFollowings">
                {{ followStatistics.followings }} following(s)
              </router-link>
              <br />
              <template v-if="!isYourself">
                <follow-button-do-follow
                  v-if="followStatistics.followed === null"
                  :user-id="user.id"
                  disabled
                />
                <follow-button-do-follow
                  v-else-if="followStatistics.followed === false"
                  :user-id="user.id"
                  @follow="onFollow"
                />
                <follow-button-unfollow
                  v-else
                  :user-id="user.id"
                  @unfollow="onUnfollow"
                />
              </template>
            </div>
          </v-flex>
        </v-flex>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { FollowStatistics, User } from '~/models'

export default Vue.extend({
  head: {},
  props: {
    user: {
      type: Object as Vue.PropType<User>,
      required: true,
    },
    isYourself: Boolean,
  },
  data: () => ({
    followStatistics: null as FollowStatistics | null,
  }),
  async fetch() {
    const response = await this.$axios.$post(`/graphql`, {
      query: `
        query ($userId: UUID!) {
          followStatistics(userId: $userId) {
            followers
            followings
            followed
          }
        }
      `,
      variables: {
        userId: this.user.id,
      },
    })

    this.followStatistics = response.data.followStatistics
  },
  computed: {
    toFollowers(): string {
      return `/users/${this.user.id}/followers`
    },
    toFollowings(): string {
      return `/users/${this.user.id}/followings`
    },
  },
  methods: {
    onFollow() {
      this.followStatistics!.followers += 1
      this.followStatistics!.followed = true
      this.$fetch()
    },
    onUnfollow() {
      this.followStatistics!.followers -= 1
      this.followStatistics!.followed = false
      this.$fetch()
    },
  },
})
</script>
