# 沸点火锅点餐管理系统（HotPot Ordering System）

一个前后端分离的火锅店点餐与门店管理系统：顾客扫码点餐、管理端处理订单与维护菜品。后端 Spring Boot 3 层架构 + MyBatis-Plus，前端 Vue 3 + Vite。

> 本项目为课程/毕业设计作品，包含完整源码、数据库建表脚本与前端工程。

## 功能

**顾客端**

- 浏览菜品、按分类筛选、关键词搜索、查看菜品详情
- 购物车增删改、选桌下单、订单记录与状态查看
- 菜品收藏、浏览历史、订单评价
- 查看门店公告

**管理端**

- 菜品管理与分类管理（含图片上传）
- 桌台管理（桌号、容纳人数、使用状态）
- 订单管理：接单、出餐、完成、取消等状态流转
- 营业数据看板：营业额、订单量、热销菜品统计
- 评价管理、公告发布、用户管理

## 技术栈

| 层次 | 技术 |
|---|---|
| 后端 | Java 17、Spring Boot 4.1.1、MyBatis-Plus 3.5.17、Lombok、Maven |
| 数据库 | MySQL 8（库名 `cs_hotpot`，建表脚本见 `DB/`） |
| 前端 | Vue 3.5、Vue Router 4、Vite 6（`frontend/` 目录） |
| 通信 | RESTful API，统一 JSON 返回结构 |

## 系统架构

```
Vue 前端页面
   │  HTTP 请求（如 POST /api/orders）
   ▼
Controller 控制层   接收请求、解析参数、组装统一返回格式
   ▼
Service 业务层      参数校验、业务规则、事务控制（@Transactional）
   ▼
Mapper 数据访问层   MyBatis-Plus 基础 CRUD + resources/mapper/*.xml 复杂查询
   ▼
MySQL 数据库
```

以"提交订单"为例：前端点击结算 → `OrderController.submit()` → `OrderService.submit()` 校验购物车、计算总价、生成订单号、写入订单与明细、清空购物车 → 返回订单号。

公共组件：

- `common/ApiResponse`：统一返回 `{ success, message, data }`，前端处理逻辑一致
- `common/GlobalExceptionHandler`：全局异常兜底，业务异常自动转换为失败响应
- `config/WebConfig`：跨域配置，允许本地前端开发服务器访问

## 目录结构

```
src/main/java/com/cs/cs_hotpot_ordering_system/
├── CsHotPotOrderingSystemApplication.java   启动类
├── common/       统一返回、全局异常处理
├── config/       跨域等配置
├── controller/   控制层（13 个控制器，按业务模块划分）
├── service/      业务层
├── mapper/       数据访问层
├── entity/       实体类（对应数据库表）
└── dto/          请求参数对象
src/main/resources/
├── mapper/       MyBatis XML
├── static/       菜品图片、头像等静态资源
└── application.example.yml   配置样例（复制为 application.yml 使用）
DB/
├── schema.sql          建表脚本（11 张表）
└── user_accounts.sql   初始账号
frontend/         Vue 3 + Vite 前端工程
```

## 快速开始

### 1. 建库与初始化

```sql
CREATE DATABASE cs_hotpot DEFAULT CHARACTER SET utf8mb4;
```

依次执行 `DB/schema.sql`、`DB/user_accounts.sql`。

### 2. 配置数据库

把 `src/main/resources/application.example.yml` 复制为 `src/main/resources/application.yml`，填入本机 MySQL 账号密码：

```yaml
spring:
  datasource:
    username: root
    password: 你的密码
    url: jdbc:mysql://localhost:3306/cs_hotpot
```

> `application.yml` 含本机数据库密码，已在 `.gitignore` 中排除，不会提交到仓库。

### 3. 启动后端

```bash
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

后端默认监听 `http://localhost:8081`。

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

Vite 开发服务器会把 `/api` 代理到 `http://localhost:8081`，按终端提示打开页面即可。

### 5. 初始账号

见 `DB/user_accounts.sql`，管理员与普通用户各一个，登录页可选择对应身份进入。

## 数据库表

| 表名 | 说明 |
|---|---|
| `user` | 用户（顾客/管理员，角色字段区分） |
| `dishCategory` | 菜品分类 |
| `dish` | 菜品 |
| `diningTable` | 桌台 |
| `orders` | 订单主表 |
| `orderItem` | 订单明细 |
| `cartItem` | 购物车 |
| `favoriteDish` | 收藏 |
| `browseHistory` | 浏览历史 |
| `dishReview` | 订单评价 |
| `announcement` | 门店公告 |

## 接口约定

所有接口以 `/api` 开头，返回统一结构：

```json
{
  "success": true,
  "message": "操作成功",
  "data": {}
}
```

## 作者

陈杉 · 武昌工学院 信息工程学院 软件工程
