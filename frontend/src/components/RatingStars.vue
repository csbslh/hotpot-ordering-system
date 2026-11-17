<script setup>
// 只负责展示平均评分：四舍五入后点亮相应数量的星星，避免把评分显示成一串普通文本。
import { computed } from 'vue'

const props = defineProps({
  value: { type: [Number, String], default: 0 }
})

const activeCount = computed(() => Math.max(0, Math.min(5, Math.round(Number(props.value) || 0))))
</script>

<template>
  <span class="rating-stars" :aria-label="Number(value || 0).toFixed(1) + ' 分'">
    <span v-for="item in 5" :key="item" :class="{ active: item <= activeCount }">★</span>
  </span>
</template>
