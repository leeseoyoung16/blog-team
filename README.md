# 📘 블로그 API

이 프로젝트는 백엔드 1명, 프론트엔드 1명이 협업하여 개발하는 소규모 블로그 서비스입니다.  

---

## ⚙️ 기술 스택
### 👩‍💻 백엔드
- Java 17
- Spring Boot 3.5.3
- Spring Web / Spring Data JPA
- JWT
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
