<script setup>
// 管理员评价管理：评价是一次订单整体用餐评价，不按菜品拆分。
import { onMounted, ref, computed } from 'vue'
import { api } from '../../utils/api'
import { averageRating, stars } from '../../utils/format'
import RatingStars from '../../components/RatingStars.vue'

const reviews = ref([])
const loading = ref(true)
const error = ref('')
const replyId = ref(null)
const replyContent = ref('')
const saving = ref(false)
const average = computed(() => averageRating(reviews.value))

async function load() {
  loading.value = true
  try { reviews.value = await api.reviews() || [] } catch (e) { error.value = e.message } finally { loading.value = false }
}
function openReply(review) { replyId.value = review.id; replyContent.value = review.replyContent || '' }
async function saveReply() {
  if (!replyContent.value.trim()) { error.value = '请输入回复内容'; return }
  saving.value = true
  try { await api.replyReview(replyId.value, { replyContent: replyContent.value.trim() }); replyId.value = null; await load() } catch (e) { error.value = e.message } finally { saving.value = false }
}
onMounted(load)
</script>

<template>
  <section class="page-section">
    <div class="page-heading"><div><h2>评价管理</h2><p>管理顾客对本次用餐的整体评价，并统一回复。</p></div><button class="ghost-button" @click="load">刷新</button></div>
    <div v-if="error" class="card danger-text">{{ error }}</div>
    <div class="stats-grid"><article class="card stat-card"><small>评价总数</small><strong>{{ reviews.length }}</strong><span>来自已完成订单</span></article><article class="card stat-card"><small>平均评价</small><strong class="warning">{{ average === '暂无' ? average : average + ' 分' }}</strong><RatingStars v-if="average !== '暂无'" :value="average" /><span v-else class="rating-empty">暂无评分</span></article></div>
    <div v-if="loading" class="card loading">正在加载评价数据...</div>
    <div v-else-if="!reviews.length" class="card empty-state">目前还没有顾客评价</div>
    <div v-else class="card">
      <article v-for="review in reviews" :key="review.id" class="review-card"><div class="avatar">{{ String(review.nickname || '客').slice(0,1) }}</div><div class="review-main"><h3>{{ review.nickname || '顾客' }} <span class="stars">{{ stars(review.rating) }}</span></h3><div class="review-meta">订单 #{{ review.orderId }} · {{ review.createdAt || '-' }} · {{ review.reviewTarget || '本次用餐' }}</div><p class="review-content">{{ review.content }}</p><div v-if="review.replyContent" class="reply-box">店家回复：{{ review.replyContent }}</div><button class="text-button" @click="openReply(review)">{{ review.replyContent ? '修改回复' : '回复评价' }}</button></div></article>
    </div>
    <div v-if="replyId" class="modal-mask" @click.self="replyId = null"><form class="modal" @submit.prevent="saveReply"><h3>回复顾客评价</h3><div class="field"><label>回复内容</label><textarea v-model="replyContent" rows="5" required placeholder="请输入店家回复"></textarea></div><div class="form-actions"><button class="ghost-button" type="button" @click="replyId = null">取消</button><button class="primary-button" :disabled="saving">{{ saving ? '提交中...' : '提交回复' }}</button></div></form></div>
  </section>
</template>
