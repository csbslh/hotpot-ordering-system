<script setup>
// 桌台管理：新增桌台及状态切换都使用 DiningTableController。
import { computed, onMounted, ref } from 'vue'
import { api } from '../../utils/api'
import { statusInfo } from '../../utils/format'

const tables = ref([])
const loading = ref(true)
const error = ref('')
const form = ref({ tableNumber: '', seatNumber: 4 })
const saving = ref(false)
const states = ['AVAILABLE', 'OCCUPIED', 'RESERVED']
const counts = computed(() => ({
  AVAILABLE: tables.value.filter(item => item.tableStatus === 'AVAILABLE').length,
  OCCUPIED: tables.value.filter(item => item.tableStatus === 'OCCUPIED').length,
  RESERVED: tables.value.filter(item => item.tableStatus === 'RESERVED').length
}))

async function load() {
  loading.value = true
  try { tables.value = await api.tables() || [] } catch (e) { error.value = e.message } finally { loading.value = false }
}

async function addTable() {
  if (!form.value.tableNumber || !form.value.seatNumber) { error.value = '请填写桌号和座位数'; return }
  saving.value = true
  try {
    await api.addTable({ tableNumber: form.value.tableNumber.trim(), seatNumber: Number(form.value.seatNumber), tableStatus: 'AVAILABLE' })
    form.value = { tableNumber: '', seatNumber: 4 }
    await load()
  } catch (e) { error.value = e.message } finally { saving.value = false }
}

async function changeStatus(table, status) {
  try { await api.updateTableStatus(table.id, status); await load() } catch (e) { error.value = e.message }
}

function status(value) { return statusInfo(value) }
onMounted(load)
</script>

<template>
  <section class="page-section">
    <div class="page-heading"><div><h2>桌台管理</h2><p>查看桌台空闲、用餐中和已预订状态。</p></div><button class="ghost-button" @click="load">刷新桌台</button></div>
    <div v-if="error" class="card danger-text">{{ error }}</div>
    <div class="table-stats-grid"><article class="card table-stat-card available"><small>空闲桌台</small><strong>{{ counts.AVAILABLE }}</strong><span class="muted-text">可供顾客选择</span></article><article class="card table-stat-card occupied"><small>用餐中</small><strong>{{ counts.OCCUPIED }}</strong><span class="muted-text">正在使用</span></article><article class="card table-stat-card reserved"><small>已预订</small><strong>{{ counts.RESERVED }}</strong><span class="muted-text">等待到店</span></article></div>
    <article class="card" style="margin-bottom:18px"><div class="card-title"><div><h3>新增桌台</h3><span>新桌台默认状态为空闲</span></div><span class="notice-icon">＋</span></div><form class="inline-form" @submit.prevent="addTable"><div class="field"><label>桌台编号</label><input v-model.trim="form.tableNumber" placeholder="例如 A01"></div><div class="field"><label>座位数</label><input v-model="form.seatNumber" type="number" min="1"></div><button class="primary-button" :disabled="saving">{{ saving ? '保存中...' : '添加桌台' }}</button></form></article>
    <div v-if="loading" class="card loading">正在加载桌台数据...</div>
    <div v-else-if="!tables.length" class="card empty-state">暂无桌台数据</div>
    <div v-else class="dining-table-grid"><article v-for="table in tables" :key="table.id" class="card dining-table-card" :class="table.tableStatus.toLowerCase()"><div class="table-card-top"><div><div class="table-card-number">{{ table.tableNumber }}</div><span class="table-card-seats">可容纳 {{ table.seatNumber }} 人</span></div><span class="badge" :class="status(table.tableStatus)[1]">{{ status(table.tableStatus)[0] }}</span></div><div class="table-card-actions"><label>修改状态</label><select class="table-card-select" :value="table.tableStatus" @change="changeStatus(table, $event.target.value)"><option v-for="item in states" :key="item" :value="item">{{ status(item)[0] }}</option></select></div><small class="muted-text">创建于 {{ table.createdAt || '-' }}</small></article></div>
  </section>
</template>
