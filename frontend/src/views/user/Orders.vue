<script setup>
// 用户订单页面：查询自己的订单并查看明细，完成订单后可跳转到整体用餐评价。
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../../utils/api'
import { getUser } from '../../utils/auth'
import { money, statusInfo } from '../../utils/format'

const router = useRouter()
const user = getUser() || {}
const orders = ref([])
const loading = ref(true)
const error = ref('')
const selected = ref(null)
const items = ref([])
const detailLoading = ref(false)

async function load() {
  loading.value = true
  try { orders.value = await api.orders(user.id) || [] } catch (e) { error.value = e.message } finally { loading.value = false }
}
async function openDetail(order) {
  selected.value = order; detailLoading.value = true
  try { items.value = await api.orderItems(order.id) || [] } catch (e) { error.value = e.message } finally { detailLoading.value = false }
}
function status(value) { return statusInfo(value) }
onMounted(load)
</script>

<template>
  <section class="page-section">
    <div class="page-heading"><div><h2>我的订单</h2><p>查看订单处理进度和本次用餐详情。</p></div><button class="ghost-button" @click="load">刷新</button></div>
    <div v-if="error" class="card danger-text">{{ error }}</div>
    <div v-if="loading" class="card loading">正在加载订单数据...</div>
    <div v-else-if="!orders.length" class="card empty-state">暂无订单，先去挑选喜欢的菜品吧。</div>
    <div v-else class="card table-scroll"><table class="data-table"><thead><tr><th>订单号</th><th>桌台</th><th>金额</th><th>状态</th><th>下单时间</th><th>操作</th></tr></thead><tbody><tr v-for="order in orders" :key="order.id"><td class="strong">{{ order.orderNumber }}</td><td>{{ order.tableNumber || '未选桌台' }}</td><td class="money">{{ money(order.totalAmount) }}</td><td><span class="badge" :class="status(order.orderStatus)[1]">{{ status(order.orderStatus)[0] }}</span></td><td>{{ order.orderedAt || '-' }}</td><td><button class="text-button" @click="openDetail(order)">查看明细</button><button v-if="order.orderStatus === 'COMPLETED'" class="text-button" @click="router.push('/user/reviews')">去评价</button></td></tr></tbody></table></div>
    <div v-if="selected" class="modal-mask" @click.self="selected = null"><div class="modal"><div class="card-title"><div><h3>订单明细</h3><span>{{ selected.orderNumber }}</span></div><button class="icon-button" @click="selected = null">关闭</button></div><p class="muted-text">下单时间：{{ selected.orderedAt || '-' }}　备注：{{ selected.remark || '无' }}</p><div v-if="detailLoading" class="loading">正在加载明细...</div><ul v-else class="order-items"><li v-for="item in items" :key="item.id"><span>{{ item.dishName }} × {{ item.quantity }}</span><b class="money">{{ money(item.subtotal) }}</b></li></ul><div class="form-actions"><b>合计：<span class="money">{{ money(selected.totalAmount) }}</span></b></div></div></div>
  </section>
</template>
