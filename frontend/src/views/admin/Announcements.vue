<script setup>
// 公告管理：管理员新增、编辑公告并控制发布状态。
import { onMounted, ref } from 'vue'
import { api } from '../../utils/api'
import { getUser } from '../../utils/auth'

const announcements = ref([])
const loading = ref(true)
const error = ref('')
const modal = ref(false)
const editingId = ref(null)
const saving = ref(false)
const form = ref({ title: '', content: '' })
const currentUser = getUser() || {}

async function load() {
  loading.value = true
  try { announcements.value = await api.announcements(true) || [] } catch (e) { error.value = e.message } finally { loading.value = false }
}
function openAdd() { editingId.value = null; form.value = { title: '', content: '' }; modal.value = true }
function openEdit(item) { editingId.value = item.id; form.value = { title: item.title || '', content: item.content || '' }; modal.value = true }
async function save() {
  if (!form.value.title.trim() || !form.value.content.trim()) { error.value = '标题和内容不能为空'; return }
  saving.value = true
  try {
    if (editingId.value) await api.updateAnnouncement(editingId.value, form.value)
    else await api.addAnnouncement({ ...form.value, publisherId: currentUser.id })
    modal.value = false
    await load()
  } catch (e) { error.value = e.message } finally { saving.value = false }
}
async function toggle(item) {
  try { await api.updateAnnouncementStatus(item.id, item.publishStatus === 1 ? 0 : 1); await load() } catch (e) { error.value = e.message }
}
onMounted(load)
</script>

<template>
  <section class="page-section">
    <div class="page-heading"><div><h2>公告管理</h2><p>发布店铺通知，普通用户端只显示已发布公告。</p></div><button class="primary-button" @click="openAdd">＋ 新增公告</button></div>
    <div v-if="error" class="card danger-text">{{ error }}</div>
    <div v-if="loading" class="card loading">正在加载公告数据...</div>
    <div v-else-if="!announcements.length" class="card empty-state">暂无公告，点击右上角新增</div>
    <div v-else><article v-for="item in announcements" :key="item.id" class="card notice-card"><div class="notice-icon">◌</div><div class="notice-main"><h3>{{ item.title }} <span class="badge" :class="item.publishStatus === 1 ? 'success' : 'warning'">{{ item.publishStatus === 1 ? '已发布' : '未发布' }}</span></h3><p>{{ item.content }}</p><small>发布时间：{{ item.publishedAt || item.createdAt || '-' }}</small><div class="form-actions"><button class="text-button" @click="openEdit(item)">编辑</button><button class="text-button" @click="toggle(item)">{{ item.publishStatus === 1 ? '下架' : '发布' }}</button></div></div></article></div>
    <div v-if="modal" class="modal-mask" @click.self="modal = false"><form class="modal" @submit.prevent="save"><h3>{{ editingId ? '编辑公告' : '新增公告' }}</h3><div class="field"><label>公告标题 *</label><input v-model.trim="form.title" required placeholder="例如：周末新品优惠"></div><div class="field" style="margin-top:14px"><label>公告内容 *</label><textarea v-model.trim="form.content" rows="7" required placeholder="请输入公告正文"></textarea></div><div class="form-actions"><button class="ghost-button" type="button" @click="modal = false">取消</button><button class="primary-button" :disabled="saving">{{ saving ? '保存中...' : '保存公告' }}</button></div></form></div>
  </section>
</template>
