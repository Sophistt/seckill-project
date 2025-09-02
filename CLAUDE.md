# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述
这是一个秒杀系统项目，专注于高并发场景下的商品秒杀功能实现。

## 项目结构
当前项目已具备基础框架，实际结构如下：

```
seckill-project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/xxxx/seckill/
│   │   │       ├── controller/          # 控制层 [已实现]
│   │   │       │   └── HomeController.java
│   │   │       ├── service/             # 业务逻辑层 [待实现]
│   │   │       ├── dao/                 # 数据访问层 [待实现]
│   │   │       ├── model/               # 实体类和数据模型 [待实现]
│   │   │       ├── config/              # 配置类 [待实现]
│   │   │       ├── utils/               # 工具类 [已实现]
│   │   │       │   └── MD5Util.java     # MD5密码加密工具
│   │   │       └── SeckillApplication.java # 主启动类
│   │   └── resources/
│   │       ├── application.yml          # 主配置文件 [已配置]
│   │       ├── mapper/                  # MyBatis映射文件 [待实现]
│   │       ├── static/                  # 静态资源目录
│   │       └── templates/               # Thymeleaf模板
│   │           ├── index.html           # 主页面
│   │           └── hello.html           # 测试页面
│   └── test/                            # 测试代码 [待实现]
│       └── java/com/xxxx/seckill/
├── pom.xml                              # Maven依赖配置 [已配置]
└── CLAUDE.md                            # 项目指导文档
```

## 核心架构设计
秒杀系统需要考虑的核心组件：

### 1. 高并发处理
- 使用Redis进行缓存和分布式锁
- 实现消息队列削峰填谷
- 数据库连接池优化

### 2. 核心模块
- **用户管理**: 用户注册、登录、权限验证
- **商品管理**: 商品信息、库存管理
- **秒杀核心**: 秒杀活动、库存扣减、订单生成
- **订单系统**: 订单处理、支付集成
- **监控统计**: 系统监控、性能统计

### 3. 技术栈现状
**已集成技术栈**:
- **后端框架**: Spring Boot 2.7.18 + Spring MVC + MyBatis-Plus 3.5.12 ✅
- **模板引擎**: Thymeleaf ✅
- **数据库**: MySQL 8.0.33 + HikariCP连接池 ✅
- **工具库**: Lombok, commons-codec, commons-lang3 ✅
- **开发工具**: Spring DevTools ✅

**待集成技术栈**:
- **缓存**: Redis ⏳
- **消息队列**: RabbitMQ 或 RocketMQ ⏳
- **负载均衡**: Nginx ⏳
- **监控**: Prometheus + Grafana ⏳

## 开发命令
项目建立后的常用命令：

```bash
# 编译项目
mvn clean compile

# 运行测试
mvn test

# 打包
mvn clean package

# 运行应用
mvn spring-boot:run

# 跳过测试打包
mvn clean package -DskipTests
```

## 关键开发注意事项
1. **库存超卖问题**: 使用悲观锁、乐观锁或分布式锁防止库存超卖
2. **接口限流**: 实现令牌桶或漏桶算法进行接口限流
3. **缓存预热**: 秒杀开始前预热商品信息到Redis
4. **异步处理**: 使用消息队列异步处理非关键路径操作
5. **数据一致性**: 确保缓存与数据库数据一致性

## 当前实现状态

### 已完成模块
1. **项目基础框架** ✅
   - Spring Boot应用主类: `SeckillApplication.java:8`
   - Maven依赖管理配置完成

2. **基础控制器** ✅
   - 首页控制器: `HomeController.java:13-19` (首页路由)
   - 测试页面控制器: `HomeController.java:21-25` (hello路由)

3. **工具类模块** ✅
   - 双重MD5密码加密: `MD5Util.java:11-49`
   - 支持前端密码转换和数据库密码存储

4. **前端页面** ✅
   - 响应式主页面设计: `templates/index.html`
   - Thymeleaf模板集成

5. **配置管理** ✅
   - 数据库连接配置: `application.yml:11-33`
   - HikariCP连接池优化配置
   - MyBatis-Plus映射配置: `application.yml:47-49`

### 待实现模块
- [ ] 用户管理模块 (注册、登录、认证)
- [ ] 商品管理模块
- [ ] 秒杀核心业务逻辑
- [ ] 订单处理模块
- [ ] Redis缓存集成
- [ ] 消息队列集成

## 性能优化要点
- 页面静态化，减少服务器渲染压力
- CDN加速静态资源访问
- 数据库读写分离
- 合理的缓存策略（缓存穿透、缓存雪崩、缓存击穿）
- 异步化处理非核心业务

## 关键配置信息
- **应用端口**: 8080
- **应用路径**: /seckill
- **包名空间**: com.xxxx.seckill
- **数据库**: seckill (需手动创建)
- **连接池**: HikariCP (最大连接数: 10)