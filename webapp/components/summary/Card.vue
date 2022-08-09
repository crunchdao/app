<template>
  <v-card link>
    <v-list-item three-line class="pr-1">
      <v-list-item-content>
        <v-list-item-title> {{ value }} </v-list-item-title>
        <v-list-item-subtitle> {{ title }} </v-list-item-subtitle>
      </v-list-item-content>
      <v-list-item-action-text style="width: 220px; height: 75px">
        <client-only>
          <v-chart :option="option" />
        </client-only>
      </v-list-item-action-text>
    </v-list-item>
  </v-card>
</template>

<script lang="ts">
import { defineComponent, provide, ref } from '@nuxtjs/composition-api'

import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
} from 'echarts/components'
import VChart, { THEME_KEY } from 'vue-echarts'

use([
  CanvasRenderer,
  LineChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
])

export default defineComponent({
  components: {
    VChart,
  },
  props: {
    title: String,
    value: [Number, String],
  },
  setup(props) {
    provide(THEME_KEY, 'dark')
    const { title, value } = props

    const option = ref({
      grid: { show: false, left: 0, right: 0, bottom: 0, top: 0 },
      backgroundColor: 'rgba(255,255,255,0)',
      xAxis: {
        show: false,
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      },
      yAxis: {
        show: false,
        type: 'value',
      },
      series: [
        {
          data: new Array(7).fill(0).map(() => Math.random()),
          type: 'line',
          smooth: true,
          areaStyle: {},
        },
      ],
    })

    return { title, value, option }
  },
})
</script>
