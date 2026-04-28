backend/
├── src/main/java/com/siheung/backend/
│   ├── BackendApplication.java
│   │
│   ├── global/
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   └── WebConfig.java
│   │   ├── exception/
│   │   └── response/
│   │       └── ApiResponse.java
│   │
│   ├── domain/
│   │   ├── auth/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   └── dto/
│   │   │
│   │   ├── user/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── entity/
│   │   │   └── dto/
│   │   │
│   │   ├── budget/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── entity/
│   │   │   └── dto/
│   │   │
│   │   ├── debt/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── entity/
│   │   │   └── dto/
│   │   │
│   │   ├── news/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   └── dto/
│   │   │
│   │   └── participation/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       ├── entity/
│   │       └── dto/
│   │
│   ├── infra/
│   │   ├── naver/
│   │   │   └── NaverNewsClient.java
│   │   └── file/
│   │       └── FinanceFileReader.java
│   │
│   └── batch/
│       └── finance/
│           ├── parser/
│           ├── processor/
│           └── loader/
│
├── src/main/resources/
│   ├── application.yml
│   ├── application-local.yml
│   └── application-prod.yml
│
├── Dockerfile
├── docker-compose.yml
├── build.gradle
└── settings.gradle

global/    공통 설정, 보안, 예외, 응답 포맷
domain/    실제 기능별 코드
infra/     외부 API, 파일 처리
batch/     재정 PDF/CSV 전처리