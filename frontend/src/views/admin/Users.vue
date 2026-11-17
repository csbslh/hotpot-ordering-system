<script setup>
// 用户管理：管理员可以查看账号、昵称、角色和状态，并启用或禁用用户。
import { onMounted, ref, computed } from 'vue'
import { api } from '../../utils/api'

const users = ref([])
const loading = ref(true)
const error = ref('')
const keyword = ref('')
const filteredUsers = computed(() => users.value.filter(user => !keyword.value || [user.username, user.nickname, user.phone].some(value => String(value || '').toLowerCase().includes(keyword.value.toLowerCase()))))

async function load() {
  loading.value = true
  try { users.value = await api.users() || [] } catch (e) { error.value = e.message } finally { loading.value = false }
}
async function changeStatus(user) {
  try { await api.updateUserStatus(user.id, user.status === 1 ? 0 : 1); await load() } catch (e) { error.value = e.message }
}
onMounted(load)
</script>

<template>
  <section class="page-section">
    <div class="page-heading"><div><h2>用户管理</h2><p>用于管理系统用户信息，支持查看用户账号、角色及状态等信息。</p></div><button class="ghost-button" @click="load">刷新用户</button></div>
    <div v-if="error" class="card danger-text">{{ error }}</div>
    <div class="toolbar"><input v-model.trim="keyword" placeholder="搜索账号、昵称或手机号"><span class="muted-text">共 {{ filteredUsers.length }} 位用户</span></div>
    <div v-if="loading" class="card loading">正在加载用户数据...</div>
    <div v-else-if="!filteredUsers.length" class="card empty-state">暂无用户数据</div>
    <div v-else class="card table-scroll"><table class="data-table"><thead><tr><th>账号</th><th>昵称</th><th>手机号</th><th>角色</th><th>状态</th><th>注册时间</th><th>操作</th></tr></thead><tbody><tr v-for="user in filteredUsers" :key="user.id"><td class="strong">{{ user.username }}</td><td>{{ user.nickname || '-' }}</td><td>{{ user.phone || '-' }}</td><td><span class="badge" :class="user.role === 'ADMIN' ? 'info' : 'success'">{{ user.role === 'ADMIN' ? '管理员' : '普通用户' }}</span></td><td><span class="badge" :class="user.status === 1 ? 'success' : 'danger'">{{ user.status === 1 ? '正常' : '已禁用' }}</span></td><td>{{ user.createdAt || '-' }}</td><td><button class="text-button" @click="changeStatus(user)">{{ user.status === 1 ? '禁用' : '启用' }}</button></td></tr></tbody></table></div>
  </section>
</template>
