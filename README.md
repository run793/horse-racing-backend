# 赛马微信小游戏 - 后端服务

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| JDK | 1.8 | 必须 |
| Spring Boot | 2.7.18 | 最后一个支持 JDK8 的版本 |
| MyBatis-Plus | 3.5.3.1 | 增强版 MyBatis，简化开发 |
| MySQL | 8.0 | 数据库 |
| Redis | - | 缓存 |
| Knife4j | 4.1.0 | Swagger 增强，接口文档 |
| Hutool | 5.8.16 | 工具库 |
| Lombok | 1.18.30 | 简化代码 |

## 项目结构

```
horse-racing-backend
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com/horseracing
│   │   │       ├── HorseRacingApplication.java   # 启动类
│   │   │       ├── common                         # 公共类
│   │   │       │   └── Result.java                # 统一响应封装
│   │   │       ├── config                         # 配置类
│   │   │       │   ├── MybatisPlusConfig.java     # MyBatis-Plus配置
│   │   │       │   ├── Knife4jConfig.java         # 接口文档配置
│   │   │       │   └── RedisConfig.java           # Redis配置
│   │   │       ├── controller                     # 控制层
│   │   │       │   └── UserController.java        # 用户接口示例
│   │   │       ├── service                        # 业务层
│   │   │       │   ├── UserService.java
│   │   │       │   └── impl
│   │   │       │       └── UserServiceImpl.java
│   │   │       ├── mapper                          # 数据访问层
│   │   │       │   └── UserMapper.java
│   │   │       ├── entity                          # 实体类
│   │   │       │   └── User.java
│   │   │       ├── dto                             # 数据传输对象
│   │   │       │   └── UserLoginDTO.java
│   │   │       ├── vo                              # 视图对象
│   │   │       │   └── UserInfoVO.java
│   │   │       └── handler                         # 处理器
│   │   │           └── GlobalExceptionHandler.java # 全局异常处理
│   │   └── resources
│   │       ├── application.yml                     # 主配置文件
│   │       ├── application-dev.yml                 # 开发环境配置
│   │       ├── application-test.yml                # 测试环境配置
│   │       └── application-prod.yml                # 生产环境配置
│   └── test
│       └── java/com/horseracing
│           └── HorseRacingApplicationTests.java
├── pom.xml
└── README.md
```

## 环境要求

- JDK 1.8+
- Maven 3.5+
- MySQL 8.0
- Redis

## 快速启动

1. 修改 `application-dev.yml` 中的数据库和 Redis 配置

2. 编译打包
```bash
mvn clean package
```

3. 运行
```bash
java -jar target/horse-racing-backend-1.0.0.jar --spring.profiles.active=dev
```

## 接口文档

启动后访问：
```
http://localhost:8080/doc.html
```

## 开发规范

- **Controller层**：接收请求、参数校验、返回响应
- **Service层**：业务逻辑处理
- **Mapper层**：数据访问，继承 BaseMapper 即可获得基础 CRUD
- **Entity**：映射数据库表
- **DTO**：接收请求参数
- **VO**：返回给前端的数据
- **统一响应**：所有接口使用 `Result<T>` 包装返回结果
- **统一异常**：所有异常由 `GlobalExceptionHandler` 统一处理

## 版本选择说明

为什么选择 Spring Boot 2.7.18：
- Spring Boot 3.x 需要 JDK 17+，不符合 JDK 1.8 要求
- Spring Boot 2.7.x 是最后一个长期支持 JDK 8 的版本，社区稳定，bug 少

为什么选择 MyBatis-Plus：
- 提供通用 CRUD，无需编写 XML，开发效率高
- 支持分页插件、逻辑删除等功能，满足大多数业务场景
- 兼容原生 MyBatis，可以混合使用

为什么选择 Knife4j：
- 基于 Swagger/SpringFox，提供更好的 UI 界面
- 支持在线接口调试，功能比原生 Swagger 更丰富
