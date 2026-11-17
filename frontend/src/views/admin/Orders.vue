<script setup>
// 管理员订单管理：状态修改直接调用后端接口，成功后重新查询订单。
import { onMounted, ref } from 'vue'
import { api } from '../../utils/api'
import { money, statusInfo } from '../../utils/format'

const orders = ref([])
const loading = ref(true)
const error = ref('')
const statusFilter = ref('')
const selectedOrder = ref(null)
const items = ref([])
const detailLoading = ref(false)
const statuses = [
  { value: 'PENDING', label: '待处理' },
  { value: 'CONFIRMED', label: '已确认' },
  { value: 'COOKING', label: '制作中' },
  { value: 'COMPLETED', label: '已完成' },
  { value: 'CANCELLED', label: '已取消' }
]

async function load() {
  loading.value = true
  error.value = ''
  try { orders.value = await api.orders() || [] } catch (e) { error.value = e.message } finally { loading.value = false }
}

function status(value) { return statusInfo(value) }
function visibleOrders() { return statusFilter.value ? orders.value.filter(item => item.orderStatus === statusFilter.value) : orders.value }

async function openDetail(order) {
  selectedOrder.value = order
  detailLoading.value = true
  items.value = []
  try { items.value = await api.orderItems(order.id) || [] } catch (e) { error.value = e.message } finally { detailLoading.value = false }
}

async function changeStatus(order, nextStatus) {
  if (order.orderStatus === nextStatus) return
  try {
    await api.updateOrderStatus(order.id, nextStatus)
    await load()
  } catch (e) { error.value = e.message }
}

onMounted(load)
</script>

<template>
  <section class="page-section">
    <div class="page-heading"><div><h2>订单管理</h2><p>确认订单、更新制作进度，订单首次确认时会同步累加菜品销量。</p></div><button class="ghost-button" @click="load">刷新</button></div>
    <div v-if="error" class="card danger-text">{{ error }}</div>
    <div class="toolbar"><select v-model="statusFilter"><option value="">全部状态</option><option v-for="item in statuses" :key="item.value" :value="item.value">{{ item.label }}</option></select><span class="muted-text">当前显示 {{ visibleOrders().length }} 笔</span></div>
    <div v-if="loading" class="card loading">正在加载订单数据...</div>
    <div v-else-if="!visibleOrders().length" class="card empty-state">暂无订单数据</div>
    <div v-else class="card table-scroll"><table class="data-table"><thead><tr><th>订单号</th><th>顾客</th><th>桌台</th><th>金额</th><th>下单时间</th><th>当前状态</th><th>处理</th><th>详情</th></tr></thead><tbody>
      <tr v-for="order in visibleOrders()" :key="order.id"><td class="strong">{{ order.orderNumber }}</td><td>{{ order.nickname || '顾客' }}</td><td>{{ order.tableNumber || '未选桌台' }}</td><td class="money">{{ money(order.totalAmount) }}</td><td>{{ order.orderedAt || '-' }}</td><td><span class="badge" :class="status(order.orderStatus)[1]">{{ status(order.orderStatus)[0] }}</span></td>
        <td><select :value="order.orderStatus" @change="changeStatus(order, $event.target.value)"><option v-for="item in statuses" :key="item.value" :value="item.value">{{ item.label }}</option></select></td><td><button class="text-button" @click="openDetail(order)">查看明细</button></td>
      </tr>
    </tbody></table></div>

    <div v-if="selectedOrder" class="modal-mask" @click.self="selectedOrder = null">
      <div class="modal">
        <div class="card-title"><div><h3>订单明细</h3><span>{{ selectedOrder.orderNumber }}</span></div><button class="icon-button" @click="selectedOrder = null">关闭</button></div>
        <p class="muted-text">顾客：{{ selectedOrder.nickname || '顾客' }}　桌台：{{ selectedOrder.tableNumber || '未选桌台' }}</p>
        <div v-if="detailLoading" class="loading">正在加载明细...</div>
        <ul v-else-if="items.length" class="order-items"><li v-for="item in items" :key="item.id"><span>{{ item.dishName }} × {{ item.quantity }}</span><b class="money">{{ money(item.subtotal) }}</b></li></ul>
        <div v-else class="empty-state">暂无订单明细</div>
        <div class="form-actions"><b>订单合计：<span class="money">{{ money(selectedOrder.totalAmount) }}</span></b></div>
      </div>
    </div>
  </section>
</template>
