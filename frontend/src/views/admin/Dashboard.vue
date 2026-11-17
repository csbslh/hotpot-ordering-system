<script setup>
// 管理员工作台：所有数字均来自 /api/dashboard/overview，不在前端写死。
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../../utils/api'
import { imageStyle, money, statusInfo } from '../../utils/format'

const router = useRouter()
const loading = ref(true)
const error = ref('')
const overview = ref({ summary: {}, latestOrders: [], popularDishes: [], tableSummary: [], dailyTrend: [] })

const summary = computed(() => overview.value.summary || {})
const trend = computed(() => overview.value.dailyTrend || [])
const maxAmount = computed(() => Math.max(1, ...trend.value.map(item => Number(item.amount || 0))))
const tableTotal = computed(() => (overview.value.tableSummary || []).reduce((sum, item) => sum + Number(item.tableCount || 0), 0))

function amount(value) {
  return money(value)
}

function status(value) {
  return statusInfo(value)
}

// SQL 返回的日期可能带有时间和时区，只展示简洁的月-日。
function dayLabel(value) {
  const text = String(value || '')
  const match = text.match(/\d{4}-\d{2}-\d{2}/)
  return match ? match[0].slice(5) : text.slice(0, 5)
}

// 图表坐标轴只展示整数金额，避免金额和 0 粘在一起。
function axisAmount(value) {
  return '¥' + Number(value || 0).toFixed(0)
}

async function load() {
  loading.value = true
  error.value = ''
  try {
    overview.value = await api.dashboard() || overview.value
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<template>
  <section class="page-section">
    <div class="page-heading">
      <div><h2>今日经营概览</h2><p>查看杉佳火锅的实时经营数据和订单情况。</p></div>
      <button class="ghost-button" type="button" @click="load">刷新数据</button>
    </div>
    <div v-if="error" class="card danger-text">{{ error }}</div>
    <div v-if="loading" class="card loading">正在加载数据库数据...</div>
    <template v-else>
      <div class="stats-grid">
        <article class="card stat-card"><small>今日营业额</small><strong class="money">{{ amount(summary.todayAmount) }}</strong><span>排除已取消订单</span></article>
        <article class="card stat-card"><small>今日订单数</small><strong>{{ summary.todayOrderCount || 0 }}</strong><span>今日已提交订单</span></article>
        <article class="card stat-card"><small>今日销售数量</small><strong class="positive">{{ summary.todayDishCount || 0 }}</strong><span>菜品累计件数</span></article>
        <article class="card stat-card"><small>进行中订单</small><strong class="warning">{{ Number(summary.pendingOrderCount || 0) + Number(summary.processingOrderCount || 0) }}</strong><span>待处理及制作中</span></article>
      </div>

      <div class="content-grid">
        <article class="card">
          <div class="card-title"><div><h3>近七日营业额</h3><span>数据来自订单表 orderedAt</span></div></div>
          <div v-if="!trend.length" class="empty-state">暂无营业趋势数据</div>
          <div v-else class="chart-wrap">
            <div class="chart-labels"><span>{{ axisAmount(maxAmount) }}</span><span>¥0</span></div>
            <div class="chart-grid"><div class="bars">
              <div v-for="item in trend" :key="item.day" class="bar-group">
                <i class="bar main-bar" :style="{ height: Math.max(5, Number(item.amount || 0) / maxAmount * 100) + '%' }"></i>
                <small>{{ dayLabel(item.day) }}</small>
              </div>
            </div></div>
          </div>
        </article>
        <article class="card">
          <div class="card-title"><div><h3>桌台状态</h3><span>当前共 {{ tableTotal }} 张桌台</span></div><button class="text-button" @click="router.push('/admin/tables')">管理桌台</button></div>
          <div v-if="!overview.tableSummary?.length" class="empty-state">暂无桌台数据</div>
          <div v-else class="dashboard-table-grid">
            <div v-for="item in overview.tableSummary" :key="item.tableStatus" class="dashboard-table-item" :class="status(item.tableStatus)[1]">
              <div class="dashboard-table-label"><i class="overview-dot" :class="{ green: item.tableStatus === 'AVAILABLE', red: item.tableStatus === 'OCCUPIED', orange: item.tableStatus === 'RESERVED' }"></i><span>{{ status(item.tableStatus)[0] }}</span></div>
              <strong>{{ item.tableCount }}</strong><small>张桌台</small>
              <div class="progress"><i :style="{ width: tableTotal ? Number(item.tableCount) / tableTotal * 100 + '%' : '0%' }"></i></div>
            </div>
          </div>
        </article>
      </div>

      <article class="card recommendation-panel">
        <div class="card-title"><div><h3>热销菜品</h3><span>按 saleNumber 从数据库倒序排列</span></div><button class="text-button" @click="router.push('/admin/dishes')">查看菜品</button></div>
        <div v-if="!overview.popularDishes?.length" class="empty-state">暂无菜品销售数据</div>
        <div v-else class="recommendation-grid">
          <div v-for="dish in overview.popularDishes" :key="dish.id" class="recommendation-card">
            <div class="mini-image" :style="imageStyle(dish.coverImage)">{{ dish.coverImage ? '' : '锅' }}</div>
            <div class="recommendation-copy"><strong>{{ dish.dishName }}</strong><small>销量 {{ dish.saleNumber || 0 }} 份</small></div><b>{{ amount(dish.price) }}</b>
          </div>
        </div>
      </article>

      <article class="card">
        <div class="card-title"><div><h3>最近订单</h3><span>最新提交的 10 笔订单</span></div><button class="text-button" @click="router.push('/admin/orders')">订单管理</button></div>
        <div v-if="!overview.latestOrders?.length" class="empty-state">暂无订单数据</div>
        <div v-else class="table-scroll"><table class="data-table"><thead><tr><th>订单号</th><th>顾客</th><th>桌台</th><th>金额</th><th>状态</th><th>下单时间</th></tr></thead><tbody>
          <tr v-for="order in overview.latestOrders" :key="order.orderNumber"><td class="strong">{{ order.orderNumber }}</td><td>{{ order.nickname || '顾客' }}</td><td>{{ order.tableNumber || '未选桌台' }}</td><td class="money">{{ amount(order.totalAmount) }}</td><td><span class="badge" :class="status(order.orderStatus)[1]">{{ status(order.orderStatus)[0] }}</span></td><td>{{ order.orderedAt || '-' }}</td></tr>
        </tbody></table></div>
      </article>
    </template>
  </section>
</template>
