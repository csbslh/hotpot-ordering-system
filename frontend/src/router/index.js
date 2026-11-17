import { createRouter, createWebHistory } from 'vue-router'
import { getUser } from '../utils/auth'
import Layout from '../layout/Layout.vue'
import Login from '../views/Login.vue'
import Dashboard from '../views/admin/Dashboard.vue'
import Dishes from '../views/admin/Dishes.vue'
import OrdersAdmin from '../views/admin/Orders.vue'
import Tables from '../views/admin/Tables.vue'
import ReviewsAdmin from '../views/admin/Reviews.vue'
import Users from '../views/admin/Users.vue'
import AnnouncementsAdmin from '../views/admin/Announcements.vue'
import ProfileAdmin from '../views/admin/Profile.vue'
import Menu from '../views/user/Menu.vue'
import Cart from '../views/user/Cart.vue'
import OrdersUser from '../views/user/Orders.vue'
import Favorites from '../views/user/Favorites.vue'
import History from '../views/user/History.vue'
import ReviewsUser from '../views/user/Reviews.vue'
import AnnouncementsUser from '../views/user/Announcements.vue'
import ProfileUser from '../views/user/Profile.vue'

const routes = [
  { path: '/', name: 'login', component: Login },
  {
    path: '/admin', component: Layout, meta: { role: 'ADMIN' },
    children: [
      { path: '', redirect: '/admin/dashboard' },
      { path: 'dashboard', component: Dashboard },
      { path: 'dishes', component: Dishes },
      { path: 'orders', component: OrdersAdmin },
      { path: 'tables', component: Tables },
      { path: 'reviews', component: ReviewsAdmin },
      { path: 'users', component: Users },
      { path: 'announcements', component: AnnouncementsAdmin },
      { path: 'profile', component: ProfileAdmin }
    ]
  },
  {
    path: '/user', component: Layout, meta: { role: 'USER' },
    children: [
      { path: '', redirect: '/user/menu' },
      { path: 'menu', component: Menu },
      { path: 'cart', component: Cart },
      { path: 'orders', component: OrdersUser },
      { path: 'favorites', component: Favorites },
      { path: 'history', component: History },
      { path: 'reviews', component: ReviewsUser },
      { path: 'announcements', component: AnnouncementsUser },
      { path: 'profile', component: ProfileUser }
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to) => {
  if (to.name === 'login') return true
  const user = getUser()
  const expectedRole = to.matched.find(item => item.meta.role)?.meta.role
  if (!user) return '/'
  if (expectedRole && String(user.role).toUpperCase() !== expectedRole) {
    return String(user.role).toUpperCase() === 'ADMIN' ? '/admin/dashboard' : '/user/menu'
  }
  return true
})

export default router
