<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../utils/api'
import { saveUser } from '../utils/auth'

const router = useRouter()
const role = ref('ADMIN')
const registerMode = ref(false)
const loading = ref(false)
const message = ref('')
const form = ref({ username: '', password: '', confirmPassword: '', nickname: '', phone: '' })

function selectRole(nextRole) {
  role.value = nextRole
  registerMode.value = false
  message.value = ''
}

async function submit() {
  message.value = ''
  if (registerMode.value) {
    if (form.value.password !== form.value.confirmPassword) { message.value = '两次输入的密码不一致'; return }
  }
  loading.value = true
  try {
    const user = registerMode.value ? await api.register(form.value) : await api.login({ username: form.value.username, password: form.value.password })
    if (!registerMode.value && String(user.role).toUpperCase() !== role.value) throw new Error('账号类型与当前登录模式不一致')
    saveUser(user)
    router.replace(String(user.role).toUpperCase() === 'ADMIN' ? '/admin/dashboard' : '/user/menu')
  } catch (error) {
    message.value = error.message || '操作未完成，请确认服务已启动后重试'
  } finally { loading.value = false }
}
</script>

<template>
  <main class="login-page">
    <section class="login-card">
      <div class="login-brand"><div class="brand-mark">杉</div><div><h1>杉佳</h1><p>火锅点餐管理系统</p></div></div>
      <div class="role-tabs"><button :class="{ active: role === 'ADMIN' }" @click="selectRole('ADMIN')">管理员登录</button><button :class="{ active: role === 'USER' }" @click="selectRole('USER')">客户登录</button></div>
      <div class="login-heading"><h2>{{ registerMode ? '注册客户账号' : `${role === 'ADMIN' ? '管理员' : '客户'}登录` }}</h2><p>{{ registerMode ? '注册后即可进入杉佳点餐页面' : '登录后进入杉佳火锅系统' }}</p></div>
      <form class="login-form" @submit.prevent="submit">
        <label>账号<input v-model.trim="form.username" required autocomplete="username" placeholder="请输入登录账号"></label>
        <label>密码<input v-model="form.password" required type="password" autocomplete="current-password" placeholder="请输入密码"></label>
        <template v-if="registerMode">
          <label>确认密码<input v-model="form.confirmPassword" required type="password" placeholder="请再次输入密码"></label>
          <label>昵称<input v-model.trim="form.nickname" placeholder="可不填写，默认使用登录账号"></label>
          <label>手机号<input v-model.trim="form.phone" placeholder="可选"></label>
        </template>
        <button class="login-submit" :disabled="loading">{{ loading ? '正在提交...' : registerMode ? '注册并登录' : '登录' }}</button>
      </form>
      <p v-if="message" class="login-message">{{ message }}</p>
      <button v-if="role === 'USER' && !registerMode" class="login-link" @click="registerMode = true">还没有客户账号？立即注册</button>
      <button v-if="registerMode" class="login-link" @click="registerMode = false">返回登录</button>
      <p class="login-footer">杉佳火锅 · 简单、方便、好吃</p>
    </section>
  </main>
</template>
