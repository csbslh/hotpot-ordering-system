<script setup>
// 用户公告页面：只读取管理员已经发布的公告。
import { onMounted, ref } from 'vue'
import { api } from '../../utils/api'

const announcements = ref([])
const loading = ref(true)
const error = ref('')
async function load() { loading.value = true; try { announcements.value = await api.announcements(false) || [] } catch (e) { error.value = e.message } finally { loading.value = false } }
onMounted(load)
</script>

<template>
  <section class="page-section"><div class="page-heading"><div><h2>店铺公告</h2><p>这里展示杉佳火锅当前已发布的店铺通知。</p></div><button class="ghost-button" @click="load">刷新</button></div><div v-if="error" class="card danger-text">{{ error }}</div><div v-if="loading" class="card loading">正在加载公告...</div><div v-else-if="!announcements.length" class="card empty-state">暂无店铺公告</div><div v-else><article v-for="item in announcements" :key="item.id" class="card notice-card"><div class="notice-icon">◌</div><div class="notice-main"><h3>{{ item.title }}</h3><p>{{ item.content }}</p><small>发布时间：{{ item.publishedAt || item.createdAt || '-' }}</small></div></article></div></section>
</template>
