<script setup>
// 菜品管理页面：管理员可以新增、修改和下架菜品，提交后重新从数据库读取列表。
import { onMounted, ref } from 'vue'
import { api } from '../../utils/api'
import { imageStyle, money } from '../../utils/format'

const dishes = ref([])
const categories = ref([])
const loading = ref(true)
const error = ref('')
const keyword = ref('')
const categoryId = ref('')
const modal = ref(false)
const editingId = ref(null)
const saving = ref(false)
const categorySaving = ref(false)
const categoryForm = ref({ categoryName: '', sortNumber: 1 })
const editingCategoryId = ref(null)
const dishForm = ref({
  categoryId: '', dishName: '', description: '', coverImage: '',
  price: '', saleNumber: 0, isRecommended: 0, isNew: 0, status: 1
})

async function load() {
  loading.value = true
  error.value = ''
  try {
    const [dishData, categoryData] = await Promise.all([
      api.dishes({ keyword: keyword.value, categoryId: categoryId.value }),
      api.categories()
    ])
    dishes.value = dishData || []
    categories.value = categoryData || []
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

function resetDish() {
  editingId.value = null
  dishForm.value = { categoryId: categories.value[0]?.id || '', dishName: '', description: '', coverImage: '', price: '', saleNumber: 0, isRecommended: 0, isNew: 0, status: 1 }
  modal.value = true
}

function editDish(dish) {
  editingId.value = dish.id
  dishForm.value = {
    categoryId: dish.categoryId || '', dishName: dish.dishName || '', description: dish.description || '',
    coverImage: dish.coverImage || '', price: dish.price ?? '', saleNumber: dish.saleNumber || 0,
    isRecommended: dish.isRecommended || 0, isNew: dish.isNew || 0, status: dish.status ?? 1
  }
  modal.value = true
}

async function saveDish() {
  if (!dishForm.value.categoryId || !dishForm.value.dishName || dishForm.value.price === '') {
    error.value = '请填写分类、菜品名称和价格'
    return
  }
  saving.value = true
  error.value = ''
  try {
    const imagePath = String(dishForm.value.coverImage || '').trim().replaceAll('\\', '/')
    const normalizedImage = imagePath.replace(/^.*?static\/dish_image\//i, '/dish_image/').replace(/^.*?static\/images\//i, '/dish_image/')
    const payload = { ...dishForm.value, coverImage: normalizedImage, categoryId: Number(dishForm.value.categoryId), price: Number(dishForm.value.price), saleNumber: Number(dishForm.value.saleNumber || 0), isRecommended: Number(dishForm.value.isRecommended || 0), isNew: Number(dishForm.value.isNew || 0), status: Number(dishForm.value.status) }
    if (editingId.value) await api.updateDish(editingId.value, payload)
    else await api.addDish(payload)
    modal.value = false
    await load()
  } catch (e) {
    error.value = e.message
  } finally {
    saving.value = false
  }
}

async function removeDish(dish) {
  if (!window.confirm('确定下架“' + dish.dishName + '”吗？')) return
  try {
    await api.deleteDish(dish.id)
    await load()
  } catch (e) { error.value = e.message }
}

function startCategory(category) {
  editingCategoryId.value = category.id
  categoryForm.value = { categoryName: category.categoryName, sortNumber: category.sortNumber || 1 }
}

function cancelCategory() {
  editingCategoryId.value = null
  categoryForm.value = { categoryName: '', sortNumber: 1 }
}

async function saveCategory() {
  if (!categoryForm.value.categoryName.trim()) { error.value = '分类名称不能为空'; return }
  categorySaving.value = true
  try {
    const payload = { categoryName: categoryForm.value.categoryName.trim(), sortNumber: Number(categoryForm.value.sortNumber || 1), status: 1 }
    if (editingCategoryId.value) await api.updateCategory(editingCategoryId.value, payload)
    else await api.addCategory(payload)
    cancelCategory()
    await load()
  } catch (e) { error.value = e.message } finally { categorySaving.value = false }
}

async function removeCategory(category) {
  if (!window.confirm('确定停用“' + category.categoryName + '”吗？')) return
  try { await api.deleteCategory(category.id); await load() } catch (e) { error.value = e.message }
}

onMounted(load)
</script>

<template>
  <section class="page-section">
    <div class="page-heading">
      <div><h2>菜品管理</h2><p>管理餐厅菜品信息，包括菜品名称、分类、价格、图片及上架状态等内容。</p></div>
      <button class="primary-button" type="button" @click="resetDish">＋ 新增菜品</button>
    </div>
    <div v-if="error" class="card danger-text">{{ error }}</div>
    <div class="toolbar">
      <input v-model="keyword" placeholder="搜索菜品名称" @keyup.enter="load">
      <select v-model="categoryId" @change="load"><option value="">全部分类</option><option v-for="item in categories" :key="item.id" :value="item.id">{{ item.categoryName }}</option></select>
      <button class="ghost-button" type="button" @click="load">搜索</button><span class="spacer"></span><span class="muted-text">共 {{ dishes.length }} 道菜品</span>
    </div>
    <div v-if="loading" class="card loading">正在加载菜品数据...</div>
    <div v-else-if="!dishes.length" class="card empty-state">暂无符合条件的菜品</div>
    <div v-else class="dish-grid">
      <article v-for="dish in dishes" :key="dish.id" class="card dish-card">
        <div class="dish-image" :class="{ 'has-image': dish.coverImage }" :style="imageStyle(dish.coverImage)">{{ dish.coverImage ? '' : '锅' }}</div>
        <div class="dish-card-body"><h3>{{ dish.dishName }}</h3><p>{{ dish.description || '暂无菜品介绍' }}</p>
          <div class="dish-card-foot"><div><b class="price">{{ money(dish.price) }}</b><small class="muted-text"> · 销量 {{ dish.saleNumber || 0 }}</small></div><span class="badge success">上架中</span></div>
          <div class="form-actions"><button class="ghost-button" type="button" @click="editDish(dish)">编辑</button><button class="text-button" type="button" @click="removeDish(dish)">下架</button></div>
        </div>
      </article>
    </div>

    <article class="card" style="margin-top:18px">
      <div class="card-title"><div><h3>菜品分类</h3><span>分类用于用户端筛选菜品</span></div></div>
      <form class="inline-form" @submit.prevent="saveCategory">
        <div class="field"><label>分类名称</label><input v-model="categoryForm.categoryName" placeholder="例如：荤菜"></div>
        <div class="field"><label>排序号</label><input v-model="categoryForm.sortNumber" type="number" min="1"></div>
        <button class="primary-button" :disabled="categorySaving">{{ categorySaving ? '保存中' : editingCategoryId ? '保存分类' : '新增分类' }}</button>
        <button v-if="editingCategoryId" class="ghost-button" type="button" @click="cancelCategory">取消</button>
      </form>
      <div v-if="categories.length" class="table-scroll" style="margin-top:16px"><table class="data-table"><thead><tr><th>分类名称</th><th>排序号</th><th>操作</th></tr></thead><tbody><tr v-for="item in categories" :key="item.id"><td>{{ item.categoryName }}</td><td>{{ item.sortNumber }}</td><td><button class="text-button" @click="startCategory(item)">编辑</button><button class="text-button" @click="removeCategory(item)">停用</button></td></tr></tbody></table></div>
    </article>

    <div v-if="modal" class="modal-mask" @click.self="modal = false">
      <form class="modal modal-wide" @submit.prevent="saveDish">
        <h3>{{ editingId ? '编辑菜品' : '新增菜品' }}</h3>
        <div class="two-column">
          <div class="field"><label>菜品名称 *</label><input v-model.trim="dishForm.dishName" required placeholder="请输入菜品名称"></div>
          <div class="field"><label>所属分类 *</label><select v-model="dishForm.categoryId" required><option value="">请选择分类</option><option v-for="item in categories" :key="item.id" :value="item.id">{{ item.categoryName }}</option></select></div>
          <div class="field"><label>价格 *</label><input v-model="dishForm.price" required type="number" min="0" step="0.01" placeholder="0.00"></div>
          <div class="field"><label>初始销量</label><input v-model="dishForm.saleNumber" type="number" min="0"></div>
          <div class="field" style="grid-column:1/-1"><label>图片地址（请填写项目路径，支持  和 /）</label><input v-model.trim="dishForm.coverImage" placeholder="src\main\resources\static\dish_image\BeefTallowBase.jpg"></div>
          <div class="field" style="grid-column:1/-1"><label>菜品介绍</label><textarea v-model.trim="dishForm.description" rows="3" placeholder="简单介绍菜品口味和分量"></textarea></div>
          <label class="switch-line"><input v-model="dishForm.isRecommended" type="checkbox" :true-value="1" :false-value="0">设为推荐菜品</label>
          <label class="switch-line"><input v-model="dishForm.isNew" type="checkbox" :true-value="1" :false-value="0">设为新品</label>
        </div>
        <div class="form-actions"><button class="ghost-button" type="button" @click="modal = false">取消</button><button class="primary-button" :disabled="saving">{{ saving ? '保存中...' : '保存菜品' }}</button></div>
      </form>
    </div>
  </section>
</template>
