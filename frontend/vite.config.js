import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      // 菜品图片由 Spring Boot 的 static/dish_image 目录提供。
      '/dish_image': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      // 用户头像由 Spring Boot 的 static/avatar_image 目录提供。
      '/avatar_image': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      // 保留旧 /images 地址，兼容已经保存到数据库的旧头像或图片。
      '/images': {
        target: 'http://localhost:8081',
        changeOrigin: true
      }
    }
  }
})
