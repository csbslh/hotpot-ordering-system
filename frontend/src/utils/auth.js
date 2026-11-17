const KEY = 'sjCurrentUser'

export function getUser() {
  try {
    return JSON.parse(localStorage.getItem(KEY) || 'null')
  } catch {
    return null
  }
}

export function saveUser(user) {
  localStorage.setItem(KEY, JSON.stringify(user))
}

export function logout() {
  localStorage.removeItem(KEY)
}

export function imageUrl(value) {
  if (!value) return ''
  let path = String(value).trim().replaceAll('\\', '/')
  if (!path) return ''
  // 支持填写项目文件路径，显示时自动转换成浏览器可访问的头像地址。
  path = path.replace(/^.*?static\/avatar_image\//i, '/avatar_image/')
  path = path.replace(/^.*?static\/images\//i, '/avatar_image/')
  if (!/^https?:\/\//i.test(path) && !path.startsWith('/')) path = `/${path}`
  return path
}

export function normalizeAvatarPath(value) {
  let path = String(value || '').trim().replaceAll('\\', '/')
  if (!path) return ''
  path = path.replace(/^.*?static\/avatar_image\//i, '/avatar_image/')
  path = path.replace(/^.*?static\/images\//i, '/avatar_image/')
  return path
}

export function displayName(user) {
  return user?.nickname || user?.username || '顾客'
}
