## ⚙️ 核心技术栈
* **底层框架**：Spring Boot 2.7.x
* **权限与安全**：Sa-Token (轻量级，拒绝 Spring Security 的繁琐)
* **数据持久层**：MyBatis-Plus 3.5.x (干掉传统 Service 接口，直接实战)
* **核心数据库**：MySQL 8.0 (引擎: InnoDB, 编码: utf8mb4)
* **极简工具箱**：Hutool, Lombok, Fastjson

##  🏃 快速开始

1.  **环境准备**：JDK 1.8+ / MySQL 8.0 / Redis (可选，依 Sa-Token 配置而定)。
2.  **数据初始化**：执行 `sql/init.sql` 导入基础表结构。
3.  **修改配置**：进入 `src/main/resources/application.yml`，修改数据库连接信息：
4.  **启动项目**：运行 `cardApiApplication.java`。



## 📦 目录结构
```text
src/main/java/com/example/cardApi/
├── common/             # 通用返回、常量、异常类
├── config/             # MyBatis-Plus、Sa-Token 配置
├── controller/         # 控制层
├── entity/             # 数据库实体（带 @Schema）
├── mapper/             # Mapper 接口及 XML
├── model/              # DTO层 (数据传输对象) 
├── query/              # 请求参数层
└── service/            # 业务实现类（无接口）
```

---
*“后端负责岁月静好，前端负责貌美如花。” ——架构寄语*