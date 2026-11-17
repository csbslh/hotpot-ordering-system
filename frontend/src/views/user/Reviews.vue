<script setup>
// 用户整体用餐评价：星星按点击位置点亮，评价对象是订单而不是单个菜品。
import { computed, onMounted, ref } from 'vue'
import { api } from '../../utils/api'
import { getUser } from '../../utils/auth'
import { averageRating, stars } from '../../utils/format'
import RatingStars from '../../components/RatingStars.vue'

const user = getUser() || {}
const reviews = ref([])
const orders = ref([])
const loading = ref(true)
const saving = ref(false)
const error = ref('')
const message = ref('')
const form = ref({ orderId: '', rating: 0, content: '' })
const average = computed(() => averageRating(reviews.value))
const reviewedOrderIds = computed(() => new Set(reviews.value.map(item => Number(item.orderId))))
const reviewableOrders = computed(() => orders.value.filter(item => item.orderStatus === 'COMPLETED' && !reviewedOrderIds.value.has(Number(item.id))))

async function load() {
  loading.value = true
  try {
    const [reviewData, orderData] = await Promise.all([api.reviews(user.id), api.orders(user.id)])
    reviews.value = reviewData || []
    orders.value = orderData || []
  } catch (e) { error.value = e.message } finally { loading.value = false }
}
function chooseRating(value) { form.value.rating = value }
async function submit() {
  if (!form.value.orderId || !form.value.rating || !form.value.content.trim()) { error.value = '请选择已完成订单、评分并填写用餐感受'; return }
  saving.value = true; error.value = ''; message.value = ''
  try { await api.addReview({ orderId: Number(form.value.orderId), userId: user.id, rating: form.value.rating, content: form.value.content.trim() }); form.value = { orderId: '', rating: 0, content: '' }; message.value = '整体用餐评价提交成功'; await load() } catch (e) { error.value = e.message } finally { saving.value = false }
}
const replyId = ref(null)
const replyContent = ref('')
const replySaving = ref(false)
function openReply(item) { replyId.value = item.id; replyContent.value = item.userReplyContent || '' }
async function submitReply() {
  if (!replyContent.value.trim()) { error.value = "请输入回复内容"; return }
  replySaving.value = true; error.value = ''
  try { await api.userReplyReview(replyId.value, { userId: user.id, userReplyContent: replyContent.value.trim() }); replyId.value = null; replyContent.value = ''; message.value = "回复提交成功"; await load() } catch (e) { error.value = e.message } finally { replySaving.value = false }
}
async function removeReview(item) {
  if (!window.confirm("确定删除这条评价吗？")) return
  try { await api.deleteReview(item.id, user.id); message.value = "回复提交成功"; await load() } catch (e) { error.value = e.message }
}
onMounted(load)
</script>

<template>
  <section class="page-section">
    <div class="page-heading"><div><h2>我的评价</h2><p>每个已完成订单可以提交一次整体用餐评价。</p></div><button class="ghost-button" @click="load">刷新</button></div>
    <p v-if="message" class="success-text">{{ message }}</p><p v-if="error" class="danger-text">{{ error }}</p>
    <div class="stats-grid"><article class="card stat-card"><small>我的评价</small><strong>{{ reviews.length }}</strong><span>条整体用餐评价</span></article><article class="card stat-card"><small>平均评价</small><strong class="warning">{{ average === '暂无' ? average : average + ' 分' }}</strong><RatingStars v-if="average !== '暂无'" :value="average" /><span v-else class="rating-empty">暂无评分</span></article></div>
    <article class="card" style="margin-bottom:18px"><div class="card-title"><div><h3>发表整体用餐评价</h3><span>评分会同步到评价管理</span></div></div><div v-if="!reviewableOrders.length" class="empty-state">暂无可评价的已完成订单</div><form v-else class="profile-form" @submit.prevent="submit"><div class="field"><label>选择订单</label><select v-model="form.orderId" required><option value="">请选择订单</option><option v-for="order in reviewableOrders" :key="order.id" :value="order.id">{{ order.orderNumber }} · {{ order.orderedAt || '' }}</option></select></div><div class="field"><label>用餐评分</label><div class="rating-picker"><button v-for="item in 5" :key="item" type="button" :class="{ active: item <= form.rating }" :aria-label="item + '星'" @click="chooseRating(item)">★</button></div><span class="muted-text">{{ form.rating ? form.rating + ' 星' : '请点击星星评分' }}</span></div><div class="field" style="margin-top:14px"><label>本次用餐感受</label><textarea v-model.trim="form.content" rows="4" required placeholder="请写下对本次用餐的整体感受"></textarea></div><div class="form-actions"><button class="primary-button" :disabled="saving">{{ saving ? '提交中...' : '提交评价' }}</button></div></form></article>
    <article class="card"><div class="card-title"><h3>历史评价</h3><span>店家回复会显示在下方</span></div><div v-if="loading" class="loading">正在加载评价...</div><div v-else-if="!reviews.length" class="empty-state">暂无历史评价</div><article v-for="item in reviews" v-else :key="item.id" class="review-card"><div class="avatar">{{ String(user.nickname || user.username || '客').slice(0,1) }}</div><div class="review-main"><h3>本次用餐 <span class="stars">{{ stars(item.rating) }}</span></h3><div class="review-meta">订单 #{{ item.orderId }} · {{ item.createdAt || '-' }}</div><p class="review-content">{{ item.content }}</p><div v-if="item.replyContent" class="reply-box">杉佳回复：{{ item.replyContent }}</div><div v-if="item.userReplyContent" class="reply-box">我的回复：{{ item.userReplyContent }}</div><div class="form-actions"><button v-if="item.replyContent" class="text-button" type="button" @click="openReply(item)">回复店家</button><button class="text-button danger-text" type="button" @click="removeReview(item)">删除评价</button></div></div></article></article>
    <div v-if="replyId" class="modal-mask"><form class="modal" @submit.prevent="submitReply"><h3>回复店家</h3><textarea v-model="replyContent" rows="4" required></textarea><div class="form-actions"><button class="ghost-button" type="button" @click="replyId = null">取消</button><button class="primary-button" :disabled="replySaving">提交回复</button></div></form></div>
  </section>
</template>
