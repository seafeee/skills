# README 编写参考

SKILL.md 已包含核心流程，本文补充：共享内容清单、编写原则、模板骨架、好/坏示例。

---

## 共享内容清单

子模块/包级 README **不应重复**以下内容（仅根 README）。需要时用相对路径引用。

| 类别 | 具体内容 | 示例 |
|------|---------|------|
| **环境与版本** | • 环境要求（JDK、构建工具）<br>• 版本兼容性（Java、框架版本范围）<br>• 技术栈（Spring Boot、Reactor等） | JDK 21+<br>Spring Boot 3.2~3.5<br>Gradle 8.5+ |
| **项目级说明** | • 整体架构设计<br>• 子项目/模块概览<br>• 全项目构建命令<br>• 依赖管理约定（BOM策略）<br>• 配置约定（前缀、格式） | `./gradlew build`<br>BOM版本统一管理<br>配置前缀：`flying` |
| **元信息** | • 许可证<br>• 行为准则 | MIT License |

**子模块/包级可写**：本模块的功能、API、配置项、使用示例、注意事项。

**引用示例**：
```markdown
环境要求见[根目录 README](../../README.md#环境要求)
```

---

## 编写原则

### 语言与风格

| 原则 | 说明 | 好示例 | 避免 |
|------|------|--------|------|
| **简洁优先** | 一句话能说清不写两句 | "本模块提供缓存工具" | "本模块提供了一套完整的、易用的缓存工具类" |
| **主动语态** | 直接说明做什么 | "本模块提供 XX" | "XX 由本模块提供" |
| **术语统一** | 同一概念用同一词 | 统一用"模块"或"子项目" | 混用"模块""子项目""组件" |
| **避免过度技术化** | 不展开框架原理 | "使用 Caffeine 缓存" | "Spring Boot 通过自动装配..." |

### 示例与配置

- **示例可直接运行**：代码可复制粘贴执行，依赖/import 已说明
- **配置真实存在**：配置项与代码对应，不虚构
- **关键配置给示例**：表格说明默认值，YAML 示例展示用法
- **同一配置项不重复说明**：避免在多个地方重复同一配置项的说明

### 关键技术处理

- **用户会用的技术**：展开说明（如缓存配置、API 使用）
- **仅内部使用的技术**：简要说明或引用文档
- **常见问题**：给出解决方案或相关学习链接

### 章节区分

| 章节 | 内容 | 是否展开版本号 |
|------|------|---------------|
| **环境要求** | 前置工具（JDK、Gradle） | ❌ 不展开 |
| **安装/依赖** | 引入方式（坐标、镜像） | ✅ 展开具体版本 |
| **技术栈** | 框架与库 | ❌ 不展开（可选展开） |
| **版本兼容性** | 版本号详细说明 | ✅ 展开，与构建脚本一致 |

---

## 模板骨架

### 根目录 README

```markdown
# 项目名称

一句话描述。

## 功能与特点
- 特点一
- 特点二

## 环境要求
- JDK XX+
- Gradle wrapper

## 安装/依赖

**Gradle**：
\`\`\`gradle
dependencies {
    implementation 'group:artifact:version'
}
\`\`\`

## 快速开始
\`\`\`bash
./gradlew build
./gradlew test
\`\`\`

## 子项目概览
| 模块 | 职责 |
|------|------|
| **module-a** | 说明 |

## 配置说明
- 配置前缀：`flying`
- 格式：YAML

## 技术栈
- Spring Boot、Reactor 等

## 版本与兼容性
- 最低 Java 版本：XX
- Spring Boot：X.x ~ X.x
```

### 子模块 README

```markdown
# 模块名称

一句话描述。

## 功能模块
- **类/接口名**：说明

## 快速使用

\`\`\`gradle
dependencies {
    implementation 'group:module:version'
}
\`\`\`

\`\`\`java
// 可运行示例
\`\`\`

## 配置说明
| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `flying.xxx.enabled` | 功能开关 | `true` |

环境要求见[根目录 README](../README.md)
```

### 包级 README

```markdown
# 包名

一句话职责。

## 用法要点
- **入口类**：`XxxService` — 说明
- **关键类**：`YyyUtil` — 说明
```

---

## 好/坏示例

### 示例1：避免重复环境和版本

**❌ 坏**（子模块重复了根README）：
```markdown
# util-cache 模块

## 环境要求  ← 重复
- JDK 21+
- Gradle 8.5+

## 技术栈  ← 重复
- Spring Boot 3.2
```

**✅ 好**（不重复，仅引用）：
```markdown
# util-cache 模块

本模块提供缓存工具，支持 Caffeine 和 Redis。

## 功能模块
- **CacheManager** - 缓存管理器

## 快速使用
\`\`\`gradle
dependencies {
    implementation 'net.flyinggroup:util-cache:2.1.0'
}
\`\`\`

环境要求和技术栈见[根目录 README](../../README.md)
```

### 示例2：配置说明真实对应

**❌ 坏**：
- 只说"支持多种配置"不列具体项
- 列了配置项但代码中不存在

**✅ 好**（表格对应代码）：
```markdown
| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `flying.cache.enabled` | 是否启用缓存 | `true` |
| `flying.cache.ttl` | 缓存过期时间（秒） | `300` |
```

### 示例3：示例可直接运行

**❌ 坏**：
```java
// 使用配置
config.setXxx(...);  // 未说明 config 从哪来，... 是什么
```

**✅ 好**：
```java
// 创建并配置缓存管理器
CacheManager manager = new CacheManager();
Cache<String, Object> cache = manager.getCache("myCache");
cache.put("key", "value");
```

### 示例4：版本与构建脚本一致

**❌ 坏**：
- 写死 Spring Boot 3.2.0，但实际由 BOM 管理

**✅ 好**：
```markdown
- 最低 Java 版本：21
- Spring Boot：3.2 ~ 3.5
- BOM 版本管理：`netFlyinggroupBomVersion`，见根 `gradle.properties`
```

### 示例5：快速开始可直接执行

**❌ 坏**：
- 虚构配置（源码中不存在的 `app.xxx.enabled`）
- 大段介绍 Spring Boot / Gradle 本身而非本项目如何用
- 只写"支持多种配置"不列具体项与示例值

**✅ 好**：
```markdown
克隆后执行：

\`\`\`bash
./gradlew :util:test --tests "net.flyinggroup.util.lang.ObjectUtilTest"
\`\`\`
```
