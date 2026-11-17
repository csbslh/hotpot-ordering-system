<script setup>
// 我的收藏：展示收藏关联的菜品，并可取消收藏或直接加入购物车。
import { onMounted, ref } from 'vue'
import { api } from '../../utils/api'
import { getUser } from '../../utils/auth'
import { imageStyle, money } from '../../utils/format'

const user = getUser() || {}
const favorites = ref([])
const loading = ref(true)
const error = ref('')
async function load() { loading.value = true; try { favorites.value = await api.favorites(user.id) || [] } catch (e) { error.value = e.message } finally { loading.value = false } }
async function remove(item) { try { await api.deleteFavorite(user.id, item.dishId); await load() } catch (e) { error.value = e.message } }
async function addCart(item) { try { await api.addCart({ userId: user.id, dishId: item.dishId, quantity: 1 }); window.alert('已加入购物车') } catch (e) { error.value = e.message } }
onMounted(load)
</script>

<template>
  <section class="page-section"><div class="page-heading"><div><h2>我的收藏</h2><p>收藏喜欢的菜品，下次点餐可以快速加入购物车。</p></div><button class="ghost-button" @click="load">刷新</button></div><div v-if="error" class="card danger-text">{{ error }}</div><div v-if="loading" class="card loading">正在加载收藏数据...</div><div v-else-if="!favorites.length" class="card empty-state">目前还没有收藏菜品。</div><div v-else class="dish-grid"><article v-for="item in favorites" :key="item.id" class="card dish-card"><div class="dish-image" :class="{ 'has-image': item.coverImage }" :style="imageStyle(item.coverImage)">{{ item.coverImage ? '' : '锅' }}</div><div class="dish-card-body"><h3>{{ item.dishName }}</h3><p>{{ item.description || '暂无菜品介绍' }}</p><div class="dish-card-foot"><b class="price">{{ money(item.price) }}</b><button class="card-add" @click="addCart(item)">加入购物车</button></div><div class="form-actions"><button class="text-button" @click="remove(item)">取消收藏</button></div></div></article></div></section>
</template>
