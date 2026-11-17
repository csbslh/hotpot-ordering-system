export function money(value) {
  return `¥ ${Number(value || 0).toFixed(2)}`
}

export function stars(value) {
  const rating = Math.max(0, Math.min(5, Number(value || 0)))
  return '★'.repeat(rating) + '☆'.repeat(5 - rating)
}

export function statusInfo(value) {
  const map = {
    PENDING: ['待处理', 'warning'], CONFIRMED: ['已确认', 'info'],
    COOKING: ['制作中', 'info'], COMPLETED: ['已完成', 'success'],
    CANCELLED: ['已取消', 'danger'], AVAILABLE: ['空闲', 'success'],
    OCCUPIED: ['用餐中', 'danger'], RESERVED: ['已预订', 'warning']
  }
  const key = String(value || '').toUpperCase()
  return map[key] || [value || '未知状态', 'info']
}

export function imageStyle(value) {
  if (!value) return {}
  let path = String(value).trim().replaceAll('\\', '/')
  // 支持填写项目文件路径，显示时自动转换成浏览器可访问的静态资源地址。
  path = path.replace(/^.*?static\/dish_image\//i, '/dish_image/')
  path = path.replace(/^.*?static\/images\//i, '/dish_image/')
  path = path.replace(/^\/?images\//i, '/dish_image/')
  if (!/^https?:\/\//i.test(path) && !path.startsWith('/')) path = `/${path}`
  return { backgroundImage: `url('${path.replaceAll("'", '%27')}')` }
}

export function averageRating(list) {
  return list.length ? (list.reduce((sum, item) => sum + Number(item.rating || 0), 0) / list.length).toFixed(1) : '暂无'
}
