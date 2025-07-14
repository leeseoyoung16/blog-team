# 📘 블로그 API

이 프로젝트는 백엔드 1명, 프론트엔드 1명이 협업하여 개발하는 소규모 블로그 서비스입니다.  

---
## 🛠️ 진행 상황

| 차시 | 항목 | 상태 | 비고 |
|------|-----------|-----|------|
| 1주차 | 기능 개발 & 유저 플로우 | ✅ | 유저 플로우 작성 |
| 2주차 | ERD & API 명세 | 🔜 | ERD 작성 및 API 명세서 작성 |
| 3주차 | Figma & 개발 시작 | 🔜 | Figma 작성 및 백엔드 기초 기능 개발 시작 |
| 4주차 | 개발 시작 | 🔜 | 미정 |

---

## ⚙️ 기술 스택
### 👩‍💻 백엔드
- Java 17
- Spring Boot 3.5.3
- Spring Web / Spring Data JPA
- JWT / Spring Security
- Lombok / Validation
- MySQL
- Postman (API 테스트용)
- JUnit 5 / AssertJ / Spring Boot Test (테스트 코드 작성용)

---

## 🔗 주요 API 명세

### 🔐 사용자 인증
| 메서드 | URI            | 설명             |
|--------|----------------|------------------|
| POST   | `/auth/signup`   | 회원 가입      |
| POST   | `/auth/login`   | 로그인      |

### 📄 게시글
| 메서드 | URI            | 설명             |
|--------|----------------|------------------|
| POST   | `/posts`   | 게시글 등록       |
| GET   | `/posts`    | 전체 게시글 조회    |
| GET   | `/posts/user/{username}`    | 특정 사용자 게시글 조회    |
| GET   | `/posts/{postId}`    | 게시글 단건 조회    |
| DELETE   | `/posts/{postId}`   | 게시글 삭제       |
| PUT   | `/posts/{postId}`   | 게시글 수정       |



---
## 📖 프로젝트 정리
[노션 페이지](https://longhaired-stove-0a0.notion.site/Blog-22fc59509498809b8429c83931d7b26a)
