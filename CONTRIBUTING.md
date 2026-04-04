# Contributing to Online Course Platform

## Project Overview

This is a web application for an online course platform built with Spring Boot. It allows teachers to manage lectures, polls, and course materials, while students can view content, participate in polls, and leave comments.

## Tech Stack

- **Framework**: Spring Boot 3.4.5
- **Language**: Java 25
- **Build Tool**: Gradle
- **Database**: H2 (in-memory)
- **ORM**: Spring Data JPA + QueryDSL
- **Views**: JSP + JSTL (Jakarta EE)
- **Security**: Spring Security
- **Internationalization**: English, Traditional Chinese (zh_TW)

## Development Setup

### Prerequisites

- Java 25
- Gradle (wrapper included)

### Running the Application

```bash
./gradlew bootRun
```

The application runs at `http://localhost:8080`

### Running Tests

```bash
./gradlew test
```

### Building a WAR File

```bash
./gradlew war
```

## TypeScript Development

The frontend uses TypeScript with esbuild for bundling.

### Prerequisites

- Node.js and npm

### Installing Dependencies

```bash
npm install
```

### Compiling TypeScript

```bash
# Type-check and generate .js and .d.ts files
npm run build:ts

# Bundle with esbuild (faster, for development)
npm run build:bundle

# Both type-check and bundle
npm run build
```

### Watch Mode (Development)

```bash
# Watch for changes with tsc
npm run watch:ts

# Watch for changes with esbuild
npm run watch:bundle
```

TypeScript source is in `src/main/webapp/ts/` and compiles to `src/main/webapp/js/`.

## Project Structure

```
src/
├── main/
│   ├── java/com/hkmu/online_course/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # MVC Controllers
│   │   ├── model/           # JPA Entities
│   │   ├── repository/      # Data access layer
│   │   │   └── impl/        # Repository implementations
│   │   ├── service/         # Business logic
│   │   │   └── impl/        # Service implementations
│   │   └── security/        # Security configuration
│   ├── resources/
│   │   ├── messages.properties        # English i18n
│   │   ├── messages_zh_TW.properties  # Traditional Chinese i18n
│   │   └── application.yaml           # App configuration
│   └── webapp/
│       └── WEB-INF/jsp/    # JSP views
└── test/
    └── java/               # Unit tests
```

## Coding Conventions

### Java

- Use interface-based programming for services and repositories
- Follow Spring Boot best practices
- Use QueryDSL for type-safe queries where applicable

### JSP Views

- Use JSTL tags and EL expressions
- Follow Bootstrap conventions for styling
- Keep logic out of views; delegate to controllers

### Naming

- Controllers: `XxxController` (e.g., `LectureController`)
- Services: `IXxxService` interface, `XxxServiceImpl` implementation
- Repositories: `IXxxRepository` interface, `XxxRepositoryImpl` implementation

### Internationalization

- All user-facing strings must be in `messages.properties` (English) and `messages_zh_TW.properties` (Traditional Chinese)
- Use `<fmt:message>` tags in JSP views

## Branch Strategy

1. Create a feature branch from `main`: `git checkout -b feature/your-feature-name`
2. Make changes and commit frequently
3. Push your branch and create a pull request when ready
4. Ensure all tests pass before merging

## Commit Messages

Use clear, descriptive commit messages:
- `feat: add user profile page`
- `fix: resolve poll voting issue`
- `docs: update README`

## Pull Requests

- Include a brief description of changes
- Reference any related issues
- Ensure tests pass
- Request review from a team member

## Reporting Issues

Report bugs or feature requests via GitHub Issues. Include:
- Clear description
- Steps to reproduce
- Expected vs actual behavior
