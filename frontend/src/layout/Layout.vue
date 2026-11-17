<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { RouterLink, RouterView, useRoute, useRouter } from 'vue-router'
import { displayName, getUser, imageUrl, logout } from '../utils/auth'

const router = useRouter()
const route = useRoute()
const user = ref(getUser() || {})

const adminMenus = [
  { to: '/admin/dashboard', label: '工作台', icon: '📊' },
  { to: '/admin/orders', label: '订单管理', icon: '📋' },
  { to: '/admin/dishes', label: '菜品管理', icon: '🍲' },
  { to: '/admin/tables', label: '桌台管理', icon: '🪑' },
  { to: '/admin/reviews', label: '评价管理', icon: '⭐' },
  { to: '/admin/users', label: '用户管理', icon: '👥' },
  { to: '/admin/announcements', label: '公告管理', icon: '📢' },
  { to: '/admin/profile', label: '用户中心', icon: '👤' }
]
const userMenus = [
  { to: '/user/menu', label: '浏览菜品', icon: '🏮' },
  { to: '/user/cart', label: '我的购物车', icon: '🛒' },
  { to: '/user/orders', label: '我的订单', icon: '📋' },
  { to: '/user/favorites', label: '我的收藏', icon: '❤️' },
  { to: '/user/history', label: '浏览记录', icon: '🕘' },
  { to: '/user/reviews', label: '我的评价', icon: '⭐' },
  { to: '/user/announcements', label: '店铺公告', icon: '📢' },
  { to: '/user/profile', label: '用户中心', icon: '👤' }
]

const isAdmin = computed(() => String(user.value.role || '').toUpperCase() === 'ADMIN')
const menus = computed(() => isAdmin.value ? adminMenus : userMenus)
const pageName = computed(() => menus.value.find(item => route.path === item.to)?.label || '用户中心')
const avatar = computed(() => imageUrl(user.value.avatar))
const firstName = computed(() => displayName(user.value).slice(0, 1))

function syncUser() {
  user.value = getUser() || {}
}

function goProfile() {
  router.push(isAdmin.value ? '/admin/profile' : '/user/profile')
}

function handleLogout() {
  logout()
  router.replace('/')
}

onMounted(() => {
  window.addEventListener('sj-user-updated', syncUser)
  window.addEventListener('storage', syncUser)
})
onUnmounted(() => {
  window.removeEventListener('sj-user-updated', syncUser)
  window.removeEventListener('storage', syncUser)
})
</script>

<template>
  <div class="app-shell">
    <aside class="sidebar">
      <div class="brand"><div class="brand-mark">杉</div><div>杉佳<small>火锅点餐管理系统</small></div></div>
      <div class="nav-label">{{ isAdmin ? '管理菜单' : '点餐服务' }}</div>
      <RouterLink v-for="item in menus" :key="item.to" :to="item.to" class="nav-item">
        <span class="nav-icon">{{ item.icon }}</span><span>{{ item.label }}</span>
      </RouterLink>
      <div class="sidebar-footer">Vue 3 + Spring Boot<br>欢迎来到杉佳火锅</div>
    </aside>

    <main class="main">
      <header class="topbar">
        <div><div class="eyebrow">杉佳火锅 / {{ isAdmin ? '管理员端' : '用户点餐端' }}</div><h1 class="page-title">{{ pageName }}</h1></div>
        <div class="top-actions">
          <span class="date-text">{{ new Date().toLocaleDateString('zh-CN') }}</span>
          <button class="user-chip" type="button" title="打开用户中心" @click="goProfile">
            <span class="avatar" :class="{ 'has-image': avatar }" :style="avatar ? { backgroundImage: `url('${avatar}')` } : {}">{{ avatar ? '' : firstName }}</span>
            <span class="user-chip-text"><b>{{ displayName(user) }}</b><small>{{ isAdmin ? '系统管理员' : '欢迎用餐' }}</small></span>
          </button>
          <button class="logout-button" type="button" @click="handleLogout">退出</button>
        </div>
      </header>
      <RouterView />
    </main>
  </div>
</template>
