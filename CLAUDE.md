# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述
这是一个秒杀系统项目，专注于高并发场景下的商品秒杀功能实现。

## 项目结构
目前项目为空，建议按照以下结构组织代码：

```
seckill-project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/seckill/
│   │   │       ├── controller/    # 控制层，处理HTTP请求
│   │   │       ├── service/       # 业务逻辑层
│   │   │       ├── dao/          # 数据访问层
│   │   │       ├── model/        # 实体类和数据模型
│   │   │       ├── config/       # 配置类
│   │   │       └── utils/        # 工具类
│   │   └── resources/
│   │       ├── application.yml   # 主配置文件
│   │       ├── mapper/          # MyBatis映射文件
│   │       └── static/          # 静态资源
│   └── test/                    # 测试代码
├── pom.xml                      # Maven依赖配置
└── README.md                    # 项目说明
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

### 3. 技术栈建议
- **后端框架**: Spring Boot + Spring MVC + MyBatis
- **数据库**: MySQL (主从复制)
- **缓存**: Redis
- **消息队列**: RabbitMQ 或 RocketMQ
- **负载均衡**: Nginx
- **监控**: Prometheus + Grafana

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

## 性能优化要点
- 页面静态化，减少服务器渲染压力
- CDN加速静态资源访问
- 数据库读写分离
- 合理的缓存策略（缓存穿透、缓存雪崩、缓存击穿）
- 异步化处理非核心业务