<script setup>
// 管理端用户中心和用户端共用同一套个人资料接口。
import { onMounted, ref } from 'vue'
import { api } from '../../utils/api'
import { displayName, getUser, imageUrl, normalizeAvatarPath, saveUser } from '../../utils/auth'

const user = ref(getUser() || {})
const profile = ref({ nickname: '', phone: '', avatar: '' })
const password = ref({ oldPassword: '', newPassword: '', confirmPassword: '' })
const profileMessage = ref('')
const profileError = ref('')
const passwordMessage = ref('')
const passwordError = ref('')
const savingProfile = ref(false)
const savingPassword = ref(false)

async function load() {
  if (!user.value.id) return
  try {
    const data = await api.profile(user.value.id)
    profile.value = { nickname: data.nickname || '', phone: data.phone || '', avatar: data.avatar || '' }
    user.value = { ...user.value, ...data }
    saveUser(user.value)
  } catch (e) { profileError.value = e.message }
}
async function updateProfile() {
  if (!profile.value.nickname.trim()) { profileError.value = '昵称不能为空'; return }
  savingProfile.value = true; profileMessage.value = ''; profileError.value = ''
  try {
    const avatarPath = normalizeAvatarPath(profile.value.avatar)
    await api.updateProfile(user.value.id, { nickname: profile.value.nickname.trim(), phone: profile.value.phone, avatar: avatarPath })
    profile.value.avatar = avatarPath
    user.value = { ...user.value, nickname: profile.value.nickname.trim(), phone: profile.value.phone, avatar: avatarPath }
    saveUser(user.value); window.dispatchEvent(new Event('sj-user-updated')); profileMessage.value = '个人资料已保存'
  } catch (e) { profileError.value = e.message } finally { savingProfile.value = false }
}
async function updatePassword() {
  if (password.value.newPassword !== password.value.confirmPassword) { passwordError.value = '两次输入的新密码不一致'; passwordMessage.value = ''; return }
  savingPassword.value = true; passwordMessage.value = ''; passwordError.value = ''
  try { await api.updatePassword(user.value.id, { oldPassword: password.value.oldPassword, newPassword: password.value.newPassword }); password.value = { oldPassword: '', newPassword: '', confirmPassword: '' }; passwordMessage.value = '密码修改成功' } catch (e) { passwordError.value = e.message } finally { savingPassword.value = false }
}
onMounted(load)
</script>

<template>
  <section class="page-section">
    <div class="page-heading"><div><h2>用户中心</h2><p>修改昵称、手机号、头像地址和登录密码。</p></div></div>
    <div class="profile-layout">
      <article class="card"><div class="profile-summary"><div class="profile-avatar" :class="{ 'has-image': imageUrl(profile.avatar) }" :style="imageUrl(profile.avatar) ? { backgroundImage: 'url(' + imageUrl(profile.avatar) + ')' } : {}">{{ imageUrl(profile.avatar) ? '' : String(displayName(user)).slice(0, 1) }}</div><div><h2>{{ displayName(user) }}</h2><p>登录账号：<strong>{{ user.username }}</strong></p><p>身份：{{ user.role === 'ADMIN' ? '系统管理员' : '普通用户' }}</p></div></div><p v-if="profileMessage" class="success-text profile-alert">{{ profileMessage }}</p><p v-if="profileError" class="danger-text profile-alert">{{ profileError }}</p>
        <form class="profile-form" @submit.prevent="updateProfile"><div class="field"><label>昵称</label><input v-model.trim="profile.nickname" required><p class="profile-help">昵称是页面显示名称，不会修改登录账号。</p></div><div class="field"><label>手机号</label><input v-model.trim="profile.phone" placeholder="可选"></div><div class="field"><label>头像地址</label><input v-model.trim="profile.avatar" placeholder="src\main\resources\static\avatar_image\avatar.jpg"><p class="profile-help">头像放入 src\main\resources\static\avatar_image 后，填写对应文件路径。</p></div><div class="avatar-preview"><div class="profile-avatar" :class="{ 'has-image': imageUrl(profile.avatar) }" :style="imageUrl(profile.avatar) ? { backgroundImage: 'url(' + imageUrl(profile.avatar) + ')' } : {}">{{ imageUrl(profile.avatar) ? '' : String(profile.nickname || '客').slice(0, 1) }}</div>当前头像预览</div><div class="form-actions"><button class="primary-button" :disabled="savingProfile">{{ savingProfile ? '保存中...' : '保存个人资料' }}</button></div></form>
      </article>
      <article class="card"><div class="profile-heading"><h2>修改登录密码</h2><p>需要输入当前密码进行验证。</p></div><p v-if="passwordMessage" class="success-text profile-alert">{{ passwordMessage }}</p><p v-if="passwordError" class="danger-text profile-alert">{{ passwordError }}</p><form class="profile-form" @submit.prevent="updatePassword"><div class="field"><label>当前密码</label><input v-model="password.oldPassword" type="password" required></div><div class="field"><label>新密码</label><input v-model="password.newPassword" type="password" minlength="4" required></div><div class="field"><label>确认新密码</label><input v-model="password.confirmPassword" type="password" required></div><div class="form-actions"><button class="primary-button" :disabled="savingPassword">{{ savingPassword ? '修改中...' : '修改密码' }}</button></div></form></article>
    </div>
  </section>
</template>
