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

### 1.3 Architecture Notes

- **JSP as Primary View Technology:** All pages rendered server-side using JSP + JSTL + EL. This is the core technology taught in the course.
- **No REST API Endpoints:** This project does NOT expose REST APIs. All user interactions (voting, commenting, form submissions) use traditional JSP form submission with redirect pattern.
- **TypeScript Scope:** TypeScript is used only for minimal client-side enhancements such as form input validation and Flowbite/Bootstrap UI interactions. It is NOT used for major application logic.
- **Flowbite Components:** UI components from Flowbite (free, open-source) are used to build pages. Components can be used directly in JSP files.

---

## 2. Data Model

### 2.1 Entity Relationship Diagram

```
┌─────────────┐     ┌─────────────┐     ┌──────────────────┐
│    User     │     │   Lecture   │     │  Course_Material │
│─────────────│     │─────────────│     │─────────────────│
│ username PK│────<│ lecture_id  │────<│ material_id PK  │
│ full_name   │     │ title       │     │ lecture_id FK   │
│ email       │     │ summary     │     │ file_name       │
│ phone_number│     │ created_at  │     │ file_extension  │
│ password    │     │ updated_at  │     │ mime_type       │
│ role        │     └─────────────┘     │ file_content BLOB│
│ status      │                          │ file_size       │
│ created_at  │     ┌─────────────┐     │ created_at      │
│ updated_at  │     │    Poll     │     │ updated_at      │
└─────────────┘     │─────────────│     └──────────────────┘
       │            │ poll_id PK  │
       │            │ question    │
       │            │ option_1~5   │
       │            │ close_time  │
       │            │ created_at  │
       │            │ updated_at  │
       │            └─────────────┘
       │                  │
       │                  │
       ▼                  ▼
┌─────────────┐     ┌─────────────┐
│    Vote     │     │   Comment   │
│─────────────│     │─────────────│
│ vote_id PK  │     │ comment_id PK│
│ poll_id FK  │     │ username FK │
│ username FK │────<│ target_id   │
│ selected_opt│     │ target_type │
│ created_at  │     │ content     │
│ updated_at  │     │ created_at  │
└─────────────┘     │ updated_at  │
                     └─────────────┘
```

### 2.2 User Table

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `username` | VARCHAR(50) | PRIMARY KEY | User login name (immutable) |
| `full_name` | VARCHAR(100) | NOT NULL | User's full name |
| `email` | VARCHAR(255) | NOT NULL | Email address |
| `phone_number` | VARCHAR(8) | NOT NULL | 8-digit phone number |
| `password` | VARCHAR(255) | NOT NULL | BCrypt encrypted password |
| `role` | INT | NOT NULL, DEFAULT 0 | 0=student, 1=teacher |
| `status` | INT | NOT NULL, DEFAULT 0 | 0=active, 1=pending, 2=disabled |
| `disabled_reason` | VARCHAR(500) | NULLABLE | Reason for disabled status |
| `created_at` | TIMESTAMP | @CreatedDate | Account creation time |
| `updated_at` | TIMESTAMP | @LastModifiedDate | Last update time |

**Status State Machine:**
- **Active (0):** Full access based on role
- **Pending (1):** Teacher awaiting approval - cannot log in
- **Disabled (2):** Account suspended

**Registration Flow:**
- Student registration → Automatically set to `active`
- Teacher registration → Set to `pending`, requires another teacher to approve via User Management page

### 2.3 Lecture Table

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `lecture_id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique lecture ID |
| `title` | VARCHAR(255) | NOT NULL | Lecture title |
| `summary` | TEXT | NOT NULL | Lecture summary/description |
| `created_at` | TIMESTAMP | @CreatedDate | Creation time |
| `updated_at` | TIMESTAMP | @LastModifiedDate | Last modification time |

### 2.4 Course_Material Table

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `material_id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique material ID |
| `lecture_id` | BIGINT | FOREIGN KEY → Lecture | Associated lecture |
| `file_name` | VARCHAR(255) | NOT NULL | Original filename (e.g., "week1_notes.pdf") |
| `file_extension` | VARCHAR(10) | NOT NULL | File extension (e.g., ".pdf", ".txt") |
| `mime_type` | VARCHAR(100) | NOT NULL | MIME type (e.g., "application/pdf") |
| `file_content` | BLOB | NOT NULL | Zstd-compressed file content |
| `file_size` | BIGINT | NOT NULL | Original file size in bytes (before compression) |
| `created_at` | TIMESTAMP | @CreatedDate | Upload time |
| `updated_at` | TIMESTAMP | @LastModifiedDate | Last modification time |

**File Storage:**
- Files are stored as BLOB in H2 database
- Content is compressed using Zstandard (zstd) before storage
- Decompression happens on download (server-side)

### 2.5 Poll Table

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `poll_id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique poll ID |
| `question` | VARCHAR(500) | NOT NULL | Poll question text |
| `option_1` | VARCHAR(255) | NOT NULL | First MC option |
| `option_2` | VARCHAR(255) | NOT NULL | Second MC option |
| `option_3` | VARCHAR(255) | NOT NULL | Third MC option |
| `option_4` | VARCHAR(255) | NOT NULL | Fourth MC option |
| `option_5` | VARCHAR(255) | NOT NULL | Fifth MC option |
| `close_time` | BIGINT | NOT NULL, DEFAULT -1 | Unix timestamp when poll closes (-1 = never) |
| `created_at` | TIMESTAMP | @CreatedDate | Creation time |
| `updated_at` | TIMESTAMP | @LastModifiedDate | Last modification time |

**Constraints:**
- Poll options (option_1~5) CANNOT be modified after poll creation
- Only the entire poll can be deleted and recreated

### 2.6 Vote Table

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `vote_id` | VARCHAR(36) | PRIMARY KEY | UUIDv7 for unique identification |
| `poll_id` | BIGINT | FOREIGN KEY → Poll, NOT NULL | Associated poll |
| `username` | VARCHAR(50) | FOREIGN KEY → User, NOT NULL | Voter |
| `selected_option` | INT | NOT NULL, CHECK (1-5) | Selected option (1-5) |
| `created_at` | TIMESTAMP | @CreatedDate | First vote time |
| `updated_at` | TIMESTAMP | @LastModifiedDate | Last vote change time |

**Constraints:**
- One vote per user per poll (enforced by unique constraint on poll_id + username)
- Users can change their vote (UPDATE instead of INSERT)

### 2.7 Comment Table

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `comment_id` | VARCHAR(36) | PRIMARY KEY | UUIDv7 for unique identification |
| `username` | VARCHAR(50) | FOREIGN KEY → User, NOT NULL | Commenter |
| `target_id` | BIGINT | NOT NULL | Target ID (lecture_id or poll_id) |
| `target_type` | VARCHAR(20) | NOT NULL, CHECK ('LECTURE', 'POLL') | Type of target |
| `content` | TEXT | NOT NULL | Comment content |
| `created_at` | TIMESTAMP | @CreatedDate | Comment creation time |
| `updated_at` | TIMESTAMP | @LastModifiedDate | Last edit time |

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
- Role-based access control (ROLE_STUDENT, ROLE_TEACHER, ROLE_ADMIN)

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
- Poll may have `close_time` - if set and past, no new votes/changes allowed

### 4.5 Comment System

- Both students and teachers can post comments
- Comments linked to either Lecture or Poll (via target_id + target_type)
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

## 7. Project Structure (Reference)

```
src/
├── main/
│   ├── java/com/course/
│   │   ├── CourseApplication.java
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   └── WebMvcConfig.java
│   │   ├── controller/
│   │   │   ├── IndexController.java
│   │   │   ├── AuthController.java
│   │   │   ├── LectureController.java
│   │   │   ├── PollController.java
│   │   │   ├── CommentController.java
│   │   │   └── AdminController.java
│   │   ├── model/
│   │   │   ├── User.java
│   │   │   ├── Lecture.java
│   │   │   ├── CourseMaterial.java
│   │   │   ├── Poll.java
│   │   │   ├── Vote.java
│   │   │   └── Comment.java
│   │   ├── repository/
│   │   ├── service/
│   │   ├── dto/
│   │   └── exception/
│   ├── resources/
│   │   ├── application.properties
│   │   ├── messages.properties
│   │   ├── messages_zh_TW.properties
│   │   └── schema.sql
│   └── webapp/
│       └── WEB-INF/
│           └── jsp/
│               ├── layout/
│               │   └── base.jsp
│               ├── index.jsp
│               ├── login.jsp
│               ├── register.jsp
│               ├── lecture/
│               │   ├── list.jsp
│               │   ├── detail.jsp
│               │   ├── create.jsp
│               │   └── edit.jsp
│               ├── poll/
│               │   ├── list.jsp
│               │   ├── detail.jsp
│               │   └── create.jsp
│               ├── user/
│               │   ├── profile.jsp
│               │   └── comments.jsp
│               └── admin/
│                   └── users.jsp
├── frontend/
│   ├── ts/
│   │   ├── main.ts          # Minimal: form validation, basic UI
│   │   └── i18n.ts          # Language switch helper (optional)
│   └── compiled/            # Compiled JS output
└── build.gradle
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
