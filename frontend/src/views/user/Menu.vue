<script setup>
// 用户点餐首页：菜品、分类、推荐、收藏和浏览记录全部与后端接口连接。
import { computed, onMounted, ref } from 'vue'
import { api } from '../../utils/api'
import { getUser } from '../../utils/auth'
import { imageStyle, money } from '../../utils/format'

const user = getUser() || {}
const dishes = ref([])
const recommendations = ref([])
const categories = ref([])
const loading = ref(true)
const error = ref('')
const keyword = ref('')
const categoryId = ref('')
const detail = ref(null)
const detailFavorite = ref(false)
const cartItems = ref([])
const cartBusyId = ref(null)
const filteredDishes = computed(() => dishes.value)
// 将购物车列表转换成“菜品编号 -> 购物数量”，卡片和弹窗都使用同一份数据。
const cartByDish = computed(() => new Map(cartItems.value.map(item => [Number(item.dishId), item])))
const cartQuantity = dishId => cartByDish.value.get(Number(dishId))?.quantity || 0

async function load() {
  loading.value = true
  error.value = ''
  try {
    const [dishData, categoryData, recommendData, cartData] = await Promise.all([
      api.dishes({ keyword: keyword.value, categoryId: categoryId.value }),
      api.categories(),
      api.recommendations({ userId: user.id, limit: 6 }),
      api.cart(user.id)
    ])
    dishes.value = dishData || []
    categories.value = categoryData || []
    recommendations.value = recommendData || []
    cartItems.value = cartData || []
  } catch (e) { error.value = e.message } finally { loading.value = false }
}

async function openDetail(dish) {
    detail.value = dish
  try {
    detailFavorite.value = Boolean(await api.favoriteExists(user.id, dish.id))
    await api.recordHistory(user.id, dish.id)
  } catch (e) { error.value = e.message }
}
async function changeCartQuantity(dish, change) {
  const current = cartByDish.value.get(Number(dish.id))
  const nextQuantity = Number(current?.quantity || 0) + change
  if (nextQuantity < 0) return
  cartBusyId.value = dish.id
  error.value = ''
  try {
    if (nextQuantity === 0 && current) await api.deleteCart(current.id, user.id)
    else if (current) await api.updateCart(current.id, user.id, nextQuantity)
    else if (nextQuantity > 0) await api.addCart({ userId: user.id, dishId: dish.id, quantity: nextQuantity })
    cartItems.value = await api.cart(user.id) || []
  } catch (e) {
    error.value = e.message
  } finally {
    cartBusyId.value = null
  }
}
async function addToCart(dish) {
  await changeCartQuantity(dish, 1)
}
async function toggleFavorite() {
  if (!detail.value) return
  try {
    if (detailFavorite.value) await api.deleteFavorite(user.id, detail.value.id)
    else await api.addFavorite(user.id, detail.value.id)
    detailFavorite.value = !detailFavorite.value
  } catch (e) { error.value = e.message }
}
onMounted(load)
</script>

<template>
  <section class="page-section">
    <div class="page-heading"><div><h2>浏览菜品</h2><p>欢迎来到杉佳火锅，浏览喜欢的菜品并开始点餐。</p></div><button class="ghost-button" @click="load">刷新菜品</button></div>
    <div v-if="error" class="card danger-text">{{ error }}</div>
    <article v-if="recommendations.length" class="card recommendation-panel"><div class="card-title"><div><h3>猜你喜欢</h3><span>根据你的点击次数推荐</span></div></div><div class="recommendation-grid"><button v-for="dish in recommendations" :key="dish.id" class="recommendation-card" @click="openDetail(dish)"><div class="mini-image" :style="imageStyle(dish.coverImage)">{{ dish.coverImage ? '' : '锅' }}</div><div class="recommendation-copy"><strong>{{ dish.dishName }}</strong><small>{{ dish.recommendationReason || '本店热门菜品' }}</small></div><b>{{ money(dish.price) }}</b></button></div></article>
    <div class="toolbar"><input v-model.trim="keyword" placeholder="搜索菜品名称" @keyup.enter="load"><button class="filter-chip" :class="{ active: categoryId === '' }" @click="categoryId = ''; load()">全部</button><button v-for="item in categories" :key="item.id" class="filter-chip" :class="{ active: String(categoryId) === String(item.id) }" @click="categoryId = item.id; load()">{{ item.categoryName }}</button><span class="spacer"></span><span class="muted-text">共 {{ filteredDishes.length }} 道</span></div>
    <div v-if="loading" class="card loading">正在加载菜品数据...</div>
    <div v-else-if="!filteredDishes.length" class="card empty-state">暂无符合条件的菜品</div>
    <div v-else class="dish-grid"><article v-for="dish in filteredDishes" :key="dish.id" class="card dish-card"><button class="dish-image" :class="{ 'has-image': dish.coverImage }" :style="imageStyle(dish.coverImage)" @click="openDetail(dish)">{{ dish.coverImage ? '' : '锅' }}</button><div class="dish-card-body"><h3>{{ dish.dishName }}</h3><p>{{ dish.description || '暂无菜品介绍' }}</p><div class="dish-card-foot"><b class="price">{{ money(dish.price) }}</b><div v-if="cartQuantity(dish.id)" class="cart-stepper" :class="{ busy: cartBusyId === dish.id }"><button type="button" aria-label="减少数量" :disabled="cartBusyId === dish.id" @click="changeCartQuantity(dish, -1)">−</button><span>{{ cartQuantity(dish.id) }}</span><button type="button" aria-label="增加数量" :disabled="cartBusyId === dish.id" @click="changeCartQuantity(dish, 1)">＋</button></div><button v-else class="card-add" :disabled="cartBusyId === dish.id" @click="addToCart(dish)">{{ cartBusyId === dish.id ? '加入中...' : '＋ 加入购物车' }}</button></div></div></article></div>
    <div v-if="detail" class="modal-mask" @click.self="detail = null"><div class="modal"><div class="card-title"><h3>{{ detail.dishName }}</h3><button class="icon-button" @click="detail = null">关闭</button></div><div class="detail-image" :class="{ 'has-image': detail.coverImage }" :style="imageStyle(detail.coverImage)">{{ detail.coverImage ? '' : '锅' }}</div><p class="review-content">{{ detail.description || '暂无菜品介绍' }}</p><div class="detail-buy-row"><b class="price">{{ money(detail.price) }}</b><div v-if="cartQuantity(detail.id)" class="cart-stepper detail-stepper" :class="{ busy: cartBusyId === detail.id }"><button type="button" aria-label="减少数量" :disabled="cartBusyId === detail.id" @click="changeCartQuantity(detail, -1)">−</button><span>{{ cartQuantity(detail.id) }}</span><button type="button" aria-label="增加数量" :disabled="cartBusyId === detail.id" @click="changeCartQuantity(detail, 1)">＋</button></div><button v-else class="card-add detail-add" :disabled="cartBusyId === detail.id" @click="addToCart(detail)">{{ cartBusyId === detail.id ? '加入中...' : '＋ 加入购物车' }}</button></div><div class="form-actions"><button class="ghost-button" @click="toggleFavorite">{{ detailFavorite ? '取消收藏' : '收藏菜品' }}</button></div></div></div>
  </section>
</template>
