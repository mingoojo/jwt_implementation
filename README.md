# JWT Implementation

프론트엔드와 백엔드에서 **JWT 인증/인가**를 구현한 학습용 프로젝트.
`Access Token`과 `Refresh Token`을 활용한 로그인/토큰 갱신 방식 구현

---

## 📂 프로젝트 구조
```
jwt_implementation/
├─ jwt_cookie_client   # Frontend (React)
└─ jwt_cookie_study    # Backend (Spring Boot)
```

---

## 🚀 기술 스택

### Frontend
- **React**
- **Axios** : API 통신
- **crypto-js** : 로컬스토리지에 저장하는 Access Token을 2차 암호화 처리
- 토큰 자동 갱신 로직 포함 (Refresh Token 기반)

### Backend
- **Spring Boot**
- **Spring Security**
- **JWT (jjwt 라이브러리)**
- **Refresh Token**: HttpOnly + Secure 쿠키에 저장  
- **Access Token**: 발급 후 프론트엔드에서 관리

---

## 🔑 인증 흐름
1. 로그인 성공 시
   - **Access Token** → 프론트엔드(LocalStorage, crypto-js로 암호화)  
   - **Refresh Token** → HttpOnly Cookie  

2. 프론트엔드에서 Axios 인터셉터로 Access Token 자동 첨부  
   - 만료 시 → Refresh Token으로 Access Token 자동 재발급  

---

## ⚙️ 실행 방법
### Backend
```bash
cd jwt_cookie_study
./gradlew bootRun
```

### FRONTEND
```bash
cd jwt_cookie_client
npm install
npm start
```
---

### 📌 기타
- 현재는 로컬 환경에서만 테스트됨
- 추후 SSL(HTTPS) 환경에서 쿠키 + 보안 테스트 예정
- 쿠키의 Secure + SameSite=None 설정이 실제 배포 시 필요

---

### ✍️ 배운 점
- Access / Refresh Token 분리 저장 패턴 학습
- Refresh Token을 쿠키에 저장하여 보안성 강화
- Axios + 인터셉터를 활용한 토큰 자동 갱신 처리
- crypto-js를 이용해 클라이언트 저장소 보안 레벨 향상
---

