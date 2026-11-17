<script setup>
// 浏览记录：记录由用户每次打开菜品详情时写入，browseCount 用于简单推荐。
import { onMounted, ref } from 'vue'
import { api } from '../../utils/api'
import { getUser } from '../../utils/auth'
import { imageStyle, money } from '../../utils/format'

const user = getUser() || {}
const history = ref([])
const loading = ref(true)
const error = ref('')
async function load() { loading.value = true; try { history.value = await api.history(user.id) || [] } catch (e) { error.value = e.message } finally { loading.value = false } }
async function clear() { if (!window.confirm('确定清空浏览记录吗？')) return; try { await api.clearHistory(user.id); await load() } catch (e) { error.value = e.message } }
onMounted(load)
</script>

<template>
  <section class="page-section"><div class="page-heading"><div><h2>浏览记录</h2><p>点击次数越多的菜品，越容易出现在猜你喜欢中。</p></div><button class="ghost-button" @click="clear">清空记录</button></div><div v-if="error" class="card danger-text">{{ error }}</div><div v-if="loading" class="card loading">正在加载浏览记录...</div><div v-else-if="!history.length" class="card empty-state">暂无浏览记录。</div><div v-else class="card table-scroll"><table class="data-table"><thead><tr><th>菜品</th><th>价格</th><th>点击次数</th><th>最后浏览时间</th></tr></thead><tbody><tr v-for="item in history" :key="item.id"><td><div class="table-status-card"><div class="dish-thumb" :class="{ 'has-image': item.coverImage }" :style="imageStyle(item.coverImage)">{{ item.coverImage ? '' : '锅' }}</div><strong>{{ item.dishName }}</strong></div></td><td class="money">{{ money(item.price) }}</td><td>{{ item.browseCount || 0 }} 次</td><td>{{ item.lastBrowsedAt || '-' }}</td></tr></tbody></table></div></section>
</template>
