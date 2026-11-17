/**
 * Vue 前端统一请求方法。
 * 后端所有 Controller 都返回 ApiResponse，因此这里统一取出 data。
 */
export async function request(url, options = {}) {
  let response
  try {
    response = await fetch(url, {
      ...options,
      headers: {
        ...(options.body ? { 'Content-Type': 'application/json' } : {}),
        ...(options.headers || {})
      }
    })
  } catch (error) {
    // 浏览器无法连接 Vite 代理或 Spring Boot 时，给出可操作的中文提示。
    throw new Error('无法连接后端服务，请确认 Spring Boot 已启动并监听 8081 端口')
  }
  const result = await response.json().catch(() => ({}))
  if (!response.ok || result.success === false) {
    throw new Error(result.message || `请求失败（${response.status}）`)
  }
  return result.data
}

export const api = {
  login: body => request('/api/auth/login', { method: 'POST', body: JSON.stringify(body) }),
  register: body => request('/api/auth/register', { method: 'POST', body: JSON.stringify(body) }),
  dishes: params => request(`/api/dishes${query(params)}`),
  dish: id => request(`/api/dishes/${id}`),
  recommendations: params => request(`/api/dishes/recommendations${query(params)}`),
  addDish: body => request('/api/dishes', { method: 'POST', body: JSON.stringify(body) }),
  updateDish: (id, body) => request(`/api/dishes/${id}`, { method: 'PUT', body: JSON.stringify(body) }),
  deleteDish: id => request(`/api/dishes/${id}`, { method: 'DELETE' }),
  categories: () => request('/api/dishCategories'),
  addCategory: body => request('/api/dishCategories', { method: 'POST', body: JSON.stringify(body) }),
  updateCategory: (id, body) => request(`/api/dishCategories/${id}`, { method: 'PUT', body: JSON.stringify(body) }),
  deleteCategory: id => request(`/api/dishCategories/${id}`, { method: 'DELETE' }),
  tables: () => request('/api/diningTables'),
  addTable: body => request('/api/diningTables', { method: 'POST', body: JSON.stringify(body) }),
  updateTableStatus: (id, status) => request(`/api/diningTables/${id}/status?tableStatus=${encodeURIComponent(status)}`, { method: 'PUT' }),
  orders: userId => request(`/api/orders${query({ userId })}`),
  orderItems: id => request(`/api/orders/${id}/items`),
  submitOrder: body => request('/api/orders', { method: 'POST', body: JSON.stringify(body) }),
  updateOrderStatus: (id, status) => request(`/api/orders/${id}/status?orderStatus=${encodeURIComponent(status)}`, { method: 'PUT' }),
  cart: userId => request(`/api/cart${query({ userId })}`),
  addCart: body => request('/api/cart', { method: 'POST', body: JSON.stringify(body) }),
  updateCart: (id, userId, quantity) => request(`/api/cart/${id}?userId=${userId}&quantity=${quantity}`, { method: 'PUT' }),
  deleteCart: (id, userId) => request(`/api/cart/${id}?userId=${userId}`, { method: 'DELETE' }),
  favorites: userId => request(`/api/favoriteDishes?userId=${userId}`),
  favoriteExists: (userId, dishId) => request(`/api/favoriteDishes/exists?userId=${userId}&dishId=${dishId}`),
  addFavorite: (userId, dishId) => request(`/api/favoriteDishes?userId=${userId}&dishId=${dishId}`, { method: 'POST' }),
  deleteFavorite: (userId, dishId) => request(`/api/favoriteDishes?userId=${userId}&dishId=${dishId}`, { method: 'DELETE' }),
  history: userId => request(`/api/browseHistories?userId=${userId}`),
  recordHistory: (userId, dishId) => request(`/api/browseHistories?userId=${userId}&dishId=${dishId}`, { method: 'POST' }),
  clearHistory: userId => request(`/api/browseHistories?userId=${userId}`, { method: 'DELETE' }),
  reviews: userId => request(`/api/reviews${query({ userId })}`),
  addReview: body => request('/api/reviews', { method: 'POST', body: JSON.stringify(body) }),
  replyReview: (id, body) => request(`/api/reviews/${id}/reply`, { method: 'PUT', body: JSON.stringify(body) }),
  deleteReview: (id, userId) => request("/api/reviews/" + id + "?userId=" + userId, { method: "DELETE" }),
  userReplyReview: (id, body) => request("/api/reviews/" + id + "/user-reply", { method: "PUT", body: JSON.stringify(body) }),
  announcements: admin => request(`/api/announcements${admin ? '?admin=true' : ''}`),
  addAnnouncement: body => request('/api/announcements', { method: 'POST', body: JSON.stringify(body) }),
  updateAnnouncement: (id, body) => request(`/api/announcements/${id}`, { method: 'PUT', body: JSON.stringify(body) }),
  updateAnnouncementStatus: (id, status) => request(`/api/announcements/${id}/status?publishStatus=${status}`, { method: 'PUT' }),
  users: () => request('/api/users'),
  updateUserStatus: (id, status) => request(`/api/users/${id}/status?status=${status}`, { method: 'PUT' }),
  profile: id => request(`/api/users/${id}/profile`),
  updateProfile: (id, body) => request(`/api/users/${id}/profile`, { method: 'PUT', body: JSON.stringify(body) }),
  updatePassword: (id, body) => request(`/api/users/${id}/password`, { method: 'PUT', body: JSON.stringify(body) }),
  dashboard: () => request('/api/dashboard/overview')
}

function query(params = {}) {
  const search = new URLSearchParams()
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') search.set(key, value)
  })
  const text = search.toString()
  return text ? `?${text}` : ''
}
