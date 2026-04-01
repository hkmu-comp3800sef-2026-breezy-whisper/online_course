# Online Course Website - Technical Requirements Specification

**Project:** COMP 3800SEF/3820SEF/S380F Group Project
**Date:** 2026-04-01
**Status:** Draft

---

## 1. Project Overview

### 1.1 Theme
Online Course Website - a web application for course content delivery, student engagement through polls, and interactive discussion via comments.

### 1.2 Technology Stack

| Layer | Technology |
|-------|------------|
| Backend Framework | Spring MVC, Spring Boot, Spring Security |
| Database | H2 Database (including BLOB for file storage) |
| View Technology | **JSP + JSTL + EL** (primary, server-side rendering) |
| Frontend Script | TypeScript (ES2015 target, minimal - form validation, basic UI effects only) |
| CSS Framework | **Tailwind CSS + Flowbite** (component library) |
| Build Tool | Gradle |
| File Compression | Zstandard (zstd) for Course Material BLOB storage |
| Java Version | 25 |

### 1.3 Architecture Notes

- **JSP as Primary View Technology:** All pages rendered server-side using JSP + JSTL + EL. This is the core technology taught in the course.
- **No REST API Endpoints:** This project does NOT expose REST APIs. All user interactions (voting, commenting, form submissions) use traditional JSP form submission with redirect pattern.
- **TypeScript Scope:** TypeScript is used only for minimal client-side enhancements such as form input validation and Flowbite/Bootstrap UI interactions. It is NOT used for major application logic.
- **Flowbite Components:** UI components from Flowbite (free, open-source) are used to build pages. Components can be used directly in JSP files.
- **JPA Auto Schema:** Database tables are automatically created/updated by Hibernate based on `@Entity` classes (`ddl-auto: update`). No manual schema.sql needed.

---

## 2. Data Model (JPA Entities)

**Note:** Tables are automatically created by Hibernate JPA based on these `@Entity` classes. No manual `schema.sql` required.

### 2.1 Entity Relationship Diagram

```
┌─────────────┐     ┌─────────────┐     ┌──────────────────┐
│    User     │     │   Lecture   │     │  Course_Material  │
│─────────────│     │─────────────│     │──────────────────│
│ username PK │────<│ lecture_id  │────<│ material_id PK   │
│ fullName    │     │ title       │     │ lectureId FK     │
│ email       │     │ summary     │     │ fileName         │
│ phoneNumber │     │ createdAt   │     │ fileExtension    │
│ password    │     │ updatedAt   │     │ mimeType         │
│ role        │     └─────────────┘     │ fileContent BLOB │
│ status      │                          │ fileSize        │
│ createdAt   │     ┌─────────────┐     │ createdAt       │
│ updatedAt    │     │    Poll     │     │ updatedAt       │
└─────────────┘     │─────────────│     └──────────────────┘
       │            │ pollId PK   │
       │            │ question    │
       │            │ option1~5   │
       │            │ closeTime   │
       │            │ createdAt   │
       │            │ updatedAt   │
       │            └─────────────┘
       │                  │
       │                  │
       ▼                  ▼
┌─────────────┐     ┌─────────────┐
│    Vote     │     │   Comment   │
│─────────────│     │─────────────│
│ voteId PK   │     │ commentId PK│
│ pollId FK   │     │ username FK │
│ username FK │────<│ targetId    │
│ selectedOpt │     │ targetType  │
│ createdAt   │     │ content     │
│ updatedAt   │     │ createdAt   │
└─────────────┘     │ updatedAt   │
                     └─────────────┘
```

### 2.2 User Entity

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 8)
    private String phoneNumber;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private Integer role = 0;  // 0=student, 1=teacher

    @Column(nullable = false)
    private Integer status = 0;  // 0=active, 1=pending, 2=disabled

    @Column(name = "disabled_reason", length = 500)
    private String disabledReason;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

**Status State Machine:**
- **Active (0):** Full access based on role
- **Pending (1):** Teacher awaiting approval - cannot log in
- **Disabled (2):** Account suspended

**Registration Flow:**
- Student registration → Automatically set to `active`
- Teacher registration → Set to `pending`, requires another teacher to approve via User Management page

### 2.3 Lecture Entity

```java
@Entity
@Table(name = "lecture")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long lectureId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 2.4 CourseMaterial Entity

```java
@Entity
@Table(name = "course_material")
public class CourseMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_id")
    private Long materialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_extension", nullable = false, length = 10)
    private String fileExtension;

    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Lob
    @Column(name = "file_content", nullable = false)
    private byte[] fileContent;  // zstd compressed

    @Column(name = "file_size", nullable = false)
    private Long fileSize;  // original size before compression

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

**File Storage:**
- Files are stored as BLOB in H2 database
- Content is compressed using Zstandard (zstd) before storage
- Decompression happens on download (server-side)

### 2.5 Poll Entity

```java
@Entity
@Table(name = "poll")
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_id")
    private Long pollId;

    @Column(nullable = false, length = 500)
    private String question;

    @Column(name = "option_1", nullable = false, length = 255)
    private String option1;

    @Column(name = "option_2", nullable = false, length = 255)
    private String option2;

    @Column(name = "option_3", nullable = false, length = 255)
    private String option3;

    @Column(name = "option_4", nullable = false, length = 255)
    private String option4;

    @Column(name = "option_5", nullable = false, length = 255)
    private String option5;

    @Column(name = "close_time", nullable = false)
    private Long closeTime = -1L;  // -1=never

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

**Constraints:**
- Poll options (option1~5) CANNOT be modified after poll creation
- Only the entire poll can be deleted and recreated

### 2.6 Vote Entity

```java
@Entity
@Table(name = "vote",
       uniqueConstraints = @UniqueConstraint(columnNames = {"poll_id", "username"}))
public class Vote {

    @Id
    @Column(name = "vote_id", length = 36)
    private String voteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @Column(name = "selected_option", nullable = false)
    private Integer selectedOption;  // 1-5

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

**Constraints:**
- One vote per user per poll (enforced by unique constraint on poll_id + username)
- Users can change their vote (UPDATE instead of INSERT)

### 2.7 Comment Entity

```java
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @Column(name = "comment_id", length = 36)
    private String commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "target_type", nullable = false, length = 20)
    private String targetType;  // "LECTURE" or "POLL"

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

**Comment Ordering:** Implementation team decides (not specified in requirements)

---

## 3. Page Structure

### 3.1 Public Pages (Unauthenticated Users)

| Page | URL | Access |
|------|-----|--------|
| Index | `/` or `/index` | Public - shows course info, lecture list, poll list |

### 3.2 Protected Pages (Authenticated Users Only)

| Page | URL | Access |
|------|-----|--------|
| Lecture Detail | `/lecture/{lectureId}` | Authenticated (Student/Teacher) |
| Poll Detail | `/poll/{pollId}` | Authenticated (Student/Teacher) |
| User Profile | `/user/profile` | Authenticated (Student/Teacher) |
| Comment History | `/user/comments` | Authenticated (Student/Teacher) |

### 3.3 Teacher-Only Pages

| Page | URL | Access |
|------|-----|--------|
| User Management | `/admin/users` | Teacher only |
| Create Lecture | `/lecture/create` | Teacher only |
| Edit Lecture | `/lecture/{lectureId}/edit` | Teacher only |
| Delete Lecture | `/lecture/{lectureId}/delete` | Teacher only |
| Create Poll | `/poll/create` | Teacher only |
| Delete Poll | `/poll/{pollId}/delete` | Teacher only |
| Create Course Material | `/lecture/{lectureId}/material/create` | Teacher only |
| Delete Course Material | `/lecture/{lectureId}/material/{materialId}/delete` | Teacher only |

---

## 4. Functionality Specification

### 4.1 Authentication & Authorization

**Registration:**
- Fields: username, password, full_name, email, phone_number, role (student/teacher)
- Students: auto-activated upon registration
- Teachers: registered as `pending` status, require existing teacher approval

**Login:**
- Username + password authentication
- Spring Security with BCrypt password encoding
- Role-based access control (ROLE_STUDENT, ROLE_TEACHER)

**User Management (Teacher only):**
- View all users
- Edit user information (except username)
- Activate pending teachers
- Disable/enable user accounts (with optional reason)

### 4.2 Index Page Features

- Course name and description
- List of all lectures (link to lecture detail pages)
- List of all polls (link to poll pages)
- Display shows multiple lectures and polls (more than one each)

### 4.3 Lecture Page Features

For each lecture:
- Lecture title
- Download links for course materials
- Brief summary of lecture content
- List of comments from all users (teachers and students)

### 4.4 Poll Page Features

For each poll:
- Poll question
- Exactly 5 MC options
- Display selected option for current user (if voted)
- Current vote count for each option
- Comment section

**Voting Rules:**
- Authenticated students/teachers can vote
- One vote per user per poll
- Users can change their vote
- Poll may have `closeTime` - if set and past, no new votes/changes allowed

### 4.5 Comment System

- Both students and teachers can post comments
- Comments linked to either Lecture or Poll (via targetId + targetType)
- Comment history page shows all comments by the current user

### 4.6 Internationalization (i18n)

**Languages:** English, Traditional Chinese

**Scope:** UI text only (buttons, labels, messages, navigation)

**Implementation:** Spring i18n with message properties files
- `messages.properties` (English - default)
- `messages_zh_TW.properties` (Traditional Chinese)

**Note:** Course content (lecture titles, summaries, poll questions) NOT translated - stored as single language.

---

## 5. Security Considerations

### 5.1 Password Storage
- BCrypt encryption with appropriate work factor
- Never store plain-text passwords

### 5.2 Access Control
- Public pages: `/`, `/login`, `/register`
- Teacher pages: protected by `ROLE_TEACHER`
- User management: `ROLE_TEACHER` only
- CSRF protection enabled for all forms

### 5.3 Input Validation
- All user inputs validated and sanitized
- File uploads validated for type and size

---

## 6. File Upload/Download Flow

### 6.1 Upload (Teacher)
1. Teacher selects file on create/edit material page
2. Server validates file (size, type)
3. File content compressed with Zstandard
4. Original filename, extension, MIME type, and compressed BLOB stored in H2
5. Original file size stored for display purposes

### 6.2 Download (Student/Teacher)
1. User clicks download link
2. Server reads BLOB from H2
3. Server decompresses content
4. Server sends decompressed file with appropriate Content-Type header
5. Browser triggers download

---

## 7. Project Structure

```
gp/                                    # Project root
├── build.gradle                      # Gradle build config (Spring Boot 4.0.5, Java 25)
├── settings.gradle
├── gradlew / gradlew.bat
├── gradle/wrapper/
│   └── gradle-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/com/hkmu/online_course/
│   │   │   ├── OnlineCourseApplication.java    # Main application class
│   │   │   ├── ServletInitializer.java        # WAR deployment support
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java        # Spring Security config (future)
│   │   │   ├── model/                          # JPA Entities
│   │   │   │   ├── User.java
│   │   │   │   ├── Lecture.java
│   │   │   │   ├── CourseMaterial.java
│   │   │   │   ├── Poll.java
│   │   │   │   ├── Vote.java
│   │   │   │   └── Comment.java
│   │   │   ├── repository/                     # Spring Data JPA Repositories
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── LectureRepository.java
│   │   │   │   ├── CourseMaterialRepository.java
│   │   │   │   ├── PollRepository.java
│   │   │   │   ├── VoteRepository.java
│   │   │   │   └── CommentRepository.java
│   │   │   ├── controller/                     # Spring MVC Controllers
│   │   │   │   ├── IndexController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── LectureController.java
│   │   │   │   ├── PollController.java
│   │   │   │   ├── CommentController.java
│   │   │   │   └── AdminController.java
│   │   │   ├── service/                       # Business logic services
│   │   │   └── dto/                            # Data Transfer Objects
│   │   ├── resources/
│   │   │   └── application.yaml                # Spring Boot config (H2, JPA, JSP)
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           └── jsp/                        # JSP view files
│   │               ├── layout/
│   │               │   └── base.jsp
│   │               ├── index.jsp
│   │               ├── login.jsp
│   │               ├── register.jsp
│   │               ├── lecture/
│   │               ├── poll/
│   │               ├── user/
│   │               └── admin/
│   └── test/
│       └── java/com/hkmu/online_course/
│           └── OnlineCourseApplicationTests.java
├── data/                                 # H2 database files (gitignored)
├── docs/
│   └── Technical Requirements Specification.md
├── Database-Setup.md
├── Requirement.md
└── .gitignore
```

### 7.1 Key Configuration Files

**application.yaml:**
```yaml
spring:
  application:
    name: online_course
  datasource:
    url: jdbc:h2:file:./data/courseDB;AUTO_RECONNECT=TRUE
    driver-class-name: org.h2.Driver
    username: sa
    password:
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    hibernate:
      ddl-auto: update    # Auto-create/update tables from @Entity
    show-sql: false
    properties:
      hibernate:
        format_sql: true
    database-platform: org.hibernate.dialect.H2Dialect
  mvc:
    view:
      prefix: /WEB-INF/jsp/
      suffix: .jsp
```

**build.gradle (key dependencies):**
```groovy
plugins {
    id 'java'
    id 'war'
    id 'org.springframework.boot' version '4.0.5'
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'com.h2database:h2:2.4.240'
    implementation 'jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.2'
    implementation 'org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1'
    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")
    providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

**Note on JSP Form Submission:**
All state-changing operations (vote, comment, upload, login, register) use traditional form POST with redirect-after-post pattern. No AJAX calls.

---

## 8. Open Questions / Implementation Decisions

| Item | Decision |
|------|----------|
| Comment ordering | Implementation team decides (newest-first or oldest-first) |
| File size limits | Implementation team defines (e.g., max 50MB per file) |
| Allowed file types | Implementation team defines (e.g., PDF, DOC, PPT, TXT) |
| Password requirements | Implementation team defines (min length, complexity) |
| Session timeout | Implementation team defines |

---

## 9. Acceptance Criteria

### 9.1 Core Features
- [ ] Students can register and log in
- [ ] Teachers can register (pending activation) and log in after activation
- [ ] Teachers can manage users (view, edit, activate, disable)
- [ ] Teachers can create/delete lectures and polls
- [ ] Teachers can upload/delete course materials
- [ ] All authenticated users can view lectures, polls, and comments
- [ ] All authenticated users can post comments on lectures and polls
- [ ] All authenticated users can vote on polls and change their vote
- [ ] All authenticated users can edit their own profile (except username)
- [ ] Users can view their comment history

### 9.2 i18n
- [ ] UI switches between English and Traditional Chinese
- [ ] Language preference persists across sessions (or defaults to English)

### 9.3 Security
- [ ] Unregistered users can only access index page
- [ ] Role-based access properly enforced
- [ ] Passwords stored as BCrypt hashes
- [ ] CSRF protection on all forms

### 9.4 File Handling
- [ ] Files uploaded by teachers are compressed and stored in H2
- [ ] Files downloaded by users are decompressed server-side
- [ ] Original filename and size preserved for download
