<template>
  <div :style="`height: ${height}px`">
    <client-only>
      <v-chart :option="option" />
    </client-only>
  </div>
</template>

<script lang="ts">
import { defineComponent, provide, ref, toRefs } from '@nuxtjs/composition-api'

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
    height: {
      type: [Number, String],
      default: 300,
    },
  },
  setup(props) {
    provide(THEME_KEY, 'dark')
    const { height } = toRefs(props)

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

    return { height, option }
  },
})
</script>
