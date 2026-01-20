# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.9 application named "tree-hole-server" (树洞服务器 - a "tree hole" is a place for anonymous sharing). It implements an AI-powered chat system with multiple personas, JWT authentication, and vector-based conversation memory. Built with Java 17 and Maven.

## Build and Run Commands

### Build
```bash
./mvnw clean package
```
Or on Windows:
```bash
mvnw.cmd clean package
```

### Run Application
```bash
./mvnw spring-boot:run
```
Or run the JAR after building:
```bash
java -jar target/tree-hole-server-0.0.1-SNAPSHOT.jar
```

### Run Tests
```bash
./mvnw test
```

### Run a Single Test
```bash
./mvnw test -Dtest=TreeHoleServerApplicationTests
```

## Technology Stack

- **Framework**: Spring Boot 3.5.9
- **Java Version**: 17
- **Build Tool**: Maven
- **Database**: PostgreSQL with pgvector extension for vector storage
- **Cache**: Redis for session and caching
- **ORM**: MyBatis Plus 3.5.9-EXT727 (with AutoTable for automatic schema generation)
- **AI**: Spring AI 1.1.2 with Tencent Hunyuan API (chat + embeddings)
- **Authentication**: JWT (jjwt 0.12.6) + Spring Security BCrypt

## Architecture Overview

The application follows a **modular, layered architecture**:

```
src/main/java/com/lwznkj/treeholeserver/
├── config/           # Spring configuration classes
├── common/           # Shared DTOs, utilities, handlers
└── module/           # Feature modules
    ├── agents/       # AI chat & persona management
    ├── auth/         # JWT authentication, UserContext
    └── users/        # User entities and mappers
```

### Key Architectural Patterns

1. **Module-Based Design**: Each domain (agents, auth, users) is a self-contained module with controller/service/mapper layers
2. **JWT Authentication**: `BearerTokenInterceptor` validates tokens and populates `UserContext` with current user info
3. **Dual Chat Memory**: JDBC-based conversation history + PgVector for semantic memory retrieval
4. **AutoTable**: MyBatis Plus extension for automatic table creation (no manual migrations needed in dev)

### AI Chat System

- **Personas**: AI personalities with system prompts, temperature, and token limits (`Persona` entity)
- **Embeddings**: Custom `HunyuanEmbeddingModel` integrates Tencent Hunyuan API for text vectorization
- **Memory**: `VectorStoreChatMemoryAdvisor` provides semantic context; JDBC repository stores conversation history
- **Default Initialization**: `DefaultPersonaInitializer` creates a default persona on startup via `application.yaml`

### Authentication Flow

1. Login returns JWT token
2. `BearerTokenInterceptor` validates bearer tokens on protected endpoints
3. Whitelist paths (configured via `auth.white-list.paths`) bypass auth
4. `UserIdAutoFillHandler` auto-fills `userId` field on entity inserts
5. `UserContext` holds current user information for request duration

## Configuration Files

- `application.yaml` - Main configuration (AI, database, Redis, auth whitelist, super admin)
- `application-dev.yaml` - Development profile settings
- `application-prod.yaml` - Production profile settings

Key configuration properties:
- `spring.ai.openai.*` - AI model settings (base URL, model name, temperature)
- `auth.white-list.paths` - Endpoints that bypass authentication
- `super-admin.*` - Default admin credentials for initialization

## Development Notes

- DevTools is enabled for hot reload during development
- Lombok annotation processing is configured in `maven-compiler-plugin`
- Active profile is set to `dev` by default (`spring.profiles.active`)
- MyBatis Plus logs SQL to stdout via `StdOutImpl`