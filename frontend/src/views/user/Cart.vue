<script setup>
// 购物车和结算页面：提交订单后由后端保存订单明细并清空购物车。
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../../utils/api'
import { getUser } from '../../utils/auth'
import { imageStyle, money } from '../../utils/format'

const user = getUser() || {}
const router = useRouter()
const items = ref([])
const tables = ref([])
const loading = ref(true)
const error = ref('')
const message = ref('')
const latestOrderId = ref(0)
const remark = ref('')
const tableId = ref('')
const savingId = ref(null)
const submitting = ref(false)
const total = computed(() => items.value.reduce((sum, item) => sum + Number(item.subtotal || 0), 0))
const availableTables = computed(() => tables.value.filter(item => item.tableStatus === 'AVAILABLE'))

async function load() {
  loading.value = true
  try {
    const [cartData, tableData] = await Promise.all([api.cart(user.id), api.tables()])
    items.value = cartData || []
    tables.value = tableData || []
    if (latestOrderId.value) {
      const orders = await api.orders(user.id)
      const latest = (orders || []).find(item => Number(item.id) === latestOrderId.value)
      if (!latest || ['COMPLETED', 'CANCELLED'].includes(latest.orderStatus)) {
        message.value = ''
        latestOrderId.value = 0
      } else {
        message.value = '订单已提交，当前正在制作中'
      }
    }
  } catch (e) { error.value = e.message } finally { loading.value = false }
}
async function changeQuantity(item, quantity) {
  if (quantity < 1) return
  savingId.value = item.id
  try { await api.updateCart(item.id, user.id, quantity); await load() } catch (e) { error.value = e.message } finally { savingId.value = null }
}
async function remove(item) {
  try { await api.deleteCart(item.id, user.id); await load() } catch (e) { error.value = e.message }
}
async function submit() {
  if (!items.value.length) { error.value = '购物车为空'; return }
  submitting.value = true
  error.value = ''
  message.value = ''
  try { await api.submitOrder({ userId: user.id, tableId: tableId.value ? Number(tableId.value) : null, remark: remark.value.trim() }); remark.value = ''; tableId.value = ''; message.value = '订单提交成功，订单已提交，请在“我的订单”中查看进度'; await load() } catch (e) { error.value = e.message } finally { submitting.value = false }
}
onMounted(load)
</script>

<template>
  <section class="page-section">
    <div class="page-heading"><div><h2>我的购物车</h2><p>确认菜品数量和桌台后提交订单。</p></div><button class="ghost-button" @click="load">刷新</button></div>
    <div v-if="message" class="card order-success"><div><strong>{{ message }}</strong><span>您可以继续浏览菜品，或到“我的订单”查看处理进度。</span></div><button class="primary-button" type="button" @click="router.push('/user/menu')">继续加菜</button></div>
    <div v-if="error" class="card danger-text">{{ error }}</div>
    <div v-if="loading" class="card loading">正在加载购物车...</div>
    <div v-else-if="!items.length" class="card empty-state">购物车暂无菜品，去浏览菜品看看吧。</div>
    <template v-else>
      <div class="content-grid"><article class="card"><div class="card-title"><h3>已选菜品</h3><span>{{ items.length }} 种</span></div><div v-for="item in items" :key="item.id" class="table-status-card" style="padding:13px 0;border-bottom:1px solid var(--line)"><div class="dish-thumb" :class="{ 'has-image': item.coverImage }" :style="imageStyle(item.coverImage)">{{ item.coverImage ? '' : '锅' }}</div><div style="flex:1"><strong>{{ item.dishName }}</strong><small class="muted-text" style="display:block;margin-top:6px">{{ money(item.price) }} / 份</small></div><div class="quantity-control"><button @click="changeQuantity(item, item.quantity - 1)">－</button><span>{{ item.quantity }}</span><button @click="changeQuantity(item, item.quantity + 1)">＋</button></div><b class="money" style="margin-left:15px">{{ money(item.subtotal) }}</b><button class="text-button" @click="remove(item)">删除</button></div></article>
        <article class="card"><div class="card-title"><h3>提交订单</h3><span>提交后进入制作流程</span></div><div class="field"><label>选择桌台</label><select v-model="tableId"><option value="">暂不选择</option><option v-for="table in availableTables" :key="table.id" :value="table.id">{{ table.tableNumber }}（{{ table.seatNumber }}人）</option></select><p class="profile-help">仅显示当前可用的桌台。</p></div><div class="field" style="margin-top:14px"><label>订单备注</label><textarea v-model="remark" rows="4" placeholder="例如：不要香菜、微辣"></textarea></div><div class="form-actions"><b>合计：<span class="price">{{ money(total) }}</span></b><button class="primary-button" type="button" :disabled="submitting" @click="submit">{{ submitting ? '提交中...' : '提交订单' }}</button></div></article></div>
    </template>
  </section>
</template>
