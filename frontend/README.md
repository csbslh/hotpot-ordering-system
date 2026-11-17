# 杉佳火锅 Vue 前端

这是基于 Vue 3 + Vite 的独立前端工程，后端接口继续使用当前 Spring Boot 项目。

## 启动

```bash
npm install
npm run dev
```

Vite 会把 `/api` 请求代理到 `http://localhost:8081`。请先启动 Spring Boot 后端，再打开终端提示的前端地址。

## 目录说明

- `src/router/index.js`：路由和登录权限控制
- `src/layout/Layout.vue`：管理端、用户端公共布局
- `src/views/admin/`：管理员页面
- `src/views/user/`：用户页面
- `src/utils/api.js`：统一请求后端接口
- `src/utils/auth.js`：登录用户缓存和退出登录

原来的 `src/main/resources/static` 页面保持不动。本工程用于后续逐步接管前端页面。
