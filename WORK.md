# Blanking Manage - 法兰下料管理系统开发规范
# 开启redis
sudo service redis-server start
# 开启minIO`:`
## 1. 确保 Docker 引擎已启动（WSL 环境必做）
sudo service docker start
## 2. 启动现有的 MinIO 容器
docker start minio
## 3.容器被删了（或者在新的地方重新开始）
### 1. 确保 WSL 内部的数据存放目录存在
mkdir -p ~/minio/data
### 2.重新创建并运行容器
docker run -p 9000:9000 -p 9001:9001 --name minio \
-e "MINIO_ROOT_USER=admin" \
-e "MINIO_ROOT_PASSWORD=password" \
-v ~/minio/data:/data \
-d minio/minio server /data --console-address ":9001"
## 🚀 核心哲学
1. **怎么简单怎么来**：拒绝过度设计，拒绝为了规范而规范。
2. **后端大数查询**：后端核心职责是高性能查询与核心逻辑计算，数据格式化与复杂 UI 逻辑交给前端。
3. **高可用原则**：所有接口必须考虑异常捕获与日志留痕，基建级报错必须有异步落库与原生日志双重保障。

---

## 🛠 技术栈
- **核心框架**：Spring Boot 2.7.x
- **权限安全**：Sa-Token
- **持久层**：MyBatis-Plus 3.5.x
- **工具类**：Lombok, Fastjson
- **数据库**：MySQL 8.0

---

## 📏 代码规范

### 1. Service 层（大兵特供版）
* **原则**：取消 `IService` 接口层，直接编写 Service 类。
* **实现**：类名以 `Service` 结尾，直接继承 `ServiceImpl<Mapper, Entity>`。
* **注解**：类上标注 `@Service`。

### 2. Controller 层
* **路径命名**：模块名小写，删除操作路径统一为 `/del`。
* **请求方式**：原则上业务操作统一使用 `@PostMapping`。
* **参数接收**：
    * **查询**：使用 `实体类名 + Query` 的 DTO 接收，必须配合 `@RequestBody`。
    * **保存**：直接使用 `Entity` 接收。
    * **删除**：使用 `List<Long> ids` 接收。
* **响应结构**：统一返回 `ApiResponse<?>`。

### 3. 数据层 (Database)
* **主键策略**：
    * 业务大表（如订单、流水）：使用 `IdType.ASSIGN_ID` (雪花算法)。
    * 系统小表（如用户、配置）：使用 `IdType.AUTO` (自增)。
* **字段约束**：
    * 金额统一使用 `DECIMAL(10,2)`，严禁使用 Float/Double。
    * 所有表必须包含 `create_time`, `update_time`, `is_deleted` (逻辑删除)。
    * 文本类字段如 `error_stack` 必须使用 `LONGTEXT`。
* **索引优化**：
    * 高频查询条件必须建立索引。
    * 利用“冗余快照字段”减少 `JOIN` 查询，提升单表扫描性能。

---

## 📝 日志与异常规范

### 1. 操作日志 (AOP)
* 使用 `@OperateLog` 注解标记 Controller 方法。
* 异步记录模块、动作、请求参数、操作人及 IP。
* **安全注意**：登录接口必须在切面中对 `password` 进行脱敏处理。

### 2. 报错日志 (Global)
* 所有未捕获异常由 `GlobalExceptionHandler` 统一处理。
* **提取关键信息**：URI、用户 ID、错误简述、完整堆栈。
* **高可用写入**：
    * 优先使用专属线程池 `logThreadPool` 异步写入 MySQL。
    * 写入 MySQL 失败时（如数据库故障），必须在本地日志文件打印 `【DB_BOMB】` 标记进行兜底。

---

## 📦 目录结构
```text
src/main/java/com/example/blankingmanage/
├── common/             # 通用返回、常量、异常类
├── config/             # 线程池、MyBatis-Plus、Sa-Token 配置
├── aspect/             # AOP 切面（日志、权限）
├── controller/         # 控制层
├── entity/             # 数据库实体（带 @Schema）
├── mapper/             # Mapper 接口及 XML
└── service/            # 业务实现类（无接口）