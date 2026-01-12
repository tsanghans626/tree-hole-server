# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.9 application named "tree-hole-server" (树洞服务器 - a "tree hole" is a metaphor for a place where people can anonymously share secrets). The project is built with Java 17 and Maven.

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
- **Key Dependencies**:
  - Spring Boot DevTools (hot reload during development)
  - Lombok (code generation for getters/setters/etc.)
  - Spring AI Alibaba Starter (1.0.0-M2.1) - AI integration

## Project Structure

- `src/main/java/com/lwznkj/treeholeserver/` - Main application code
  - `TreeHoleServerApplication.java` - Spring Boot entry point
- `src/main/resources/` - Configuration files
  - `application.properties` - Application configuration
- `src/test/java/com/lwznkj/treeholeserver/` - Test code

## Development Notes

- Lombok annotation processing is configured in `maven-compiler-plugin`
- The project uses Spring Milestones repository for Spring dependencies
- DevTools is enabled for automatic restart on file changes