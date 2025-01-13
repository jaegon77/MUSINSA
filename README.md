# MUSNISA Backend Project

## 프로젝트 개요

Musinsa 백엔드 과제 프로젝트는 최신 기술 스택을 활용하여 효율적이고 확장 가능한 상품 관리 시스템을 개발하기 위한 프로젝트입니다.
이 프로젝트는 다음과 같은 핵심 목표를 가지고 설계 및 구현되었습니다.

- **최신 기술 스택 활용**: JDK 21(가상 스레드), Spring Boot 3.4.1, QueryDSL.
- **API 개발 및 테스트**: 상품, 카테고리, 브랜드 관리 기능 제공.
- **확장 가능한 설계**: RESTful API, 통합 테스트 및 유닛 테스트 기반.
- **자동화된 데이터 관리**: `schema.sql` 및 `data.sql`을 사용한 초기 데이터 설정.

---

## 구현 범위

### 주요 기능

1. **카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회**
    - 카테고리 별 최저가 상품과 브랜드 정보 및 총합 금액 조회.
2. **브랜드 별 최저가 상품 조회**
    - 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을
      조회.
3. **카테고리 내 최고/최저가 상품 조회**
    - 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
4. **브랜드 관리**
    - 브랜드 추가, 수정, 삭제.
5. **카테고리 관리**
    - 카테고리 추가, 수정, 삭제.
6. **상품 관리**
    - 상품 추가, 수정, 삭제.

---

## 프로젝트 실행 방법

### 사전 요구 사항
- JDK 21
- Gradle 8.11.1
- H2 Database (Embedded)
- IntelliJ IDEA

### 실행 방법

1. **소스 코드 클론**
   ```bash
   git clone https://github.com/jaegon77/MUSINSA.git
   cd MUSINSA
   ```

2. **필수 의존성 설치**
   ```bash
   ./gradlew build
   ```

3. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```
   애플리케이션 실행 후 다음 URL에 접속하여 확인합니다.
    - Swagger UI: [http://localhost:7007/category/swagger-ui/index.html](http://localhost:7007/category/swagger-ui/index.html)
    - API Docs: [http://localhost:7007/category/api-docs](http://localhost:7007/category/api-docs)

4. **테스트 실행**
   ```bash
   ./gradlew test
   ```
    - 테스트 결과는 `build/reports/tests/test/index.html`에서 확인 가능합니다.

---

## 기술 스택

- **Java**: JDK 21 (Virtual Threads 지원)
- **Spring Boot**: 3.4.1
- **Database**: H2 (In-Memory)
- **QueryDSL**: 효율적인 쿼리 작성 및 동적 쿼리 지원
- **Swagger**: API 문서화 (Springdoc OpenAPI)
- **Build Tool**: Gradle
- **Testing**: JUnit 5

---

## 프로젝트 구조

```plaintext
src
├── main
│   ├── java
│   │   └── com.example.musinsa
│   │       ├── controller
│   │       ├── service
│   │       ├── repository
│   │       ├── entity
│   │       ├── dto
│   │       ├── common
│   │       │   ├── aop
│   │       │   ├── config
│   │       │   ├── constant
│   │       │   ├── exception
│   │       │   └── util
│   │       └── MusinsaApplication.java
│   └── resources
│       ├── application.yml
│       ├── schema.sql
│       ├── data.sql
│       ├── static
│       └── templates
└── test
    ├── java
    │   └── com.example.musinsa
    │       ├── controller
    │       ├── service
    │       └── repository
    └── resources
        ├── application-test.yml
        ├── schema.sql
        └── data.sql
```

---

## 설정 파일

### `application.yml`

```yaml
server:
  port: 7007
  servlet:
    context-path: /category
  undertow:
    io-threads: 4
    worker-threads: 100
  max-http-request-header-size: 65536

spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:testdb
    username: gon
    password: password
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        dialect: org.hibernate.dialect.H2Dialect
        show_sql: true
        format_sql: true

springdoc:
  swagger-ui:
    operations-sorter: method
    display-request-duration: true
  api-docs:
    path: /api-docs
```

---

## 데이터베이스 설정

### Schema Definition (`schema.sql`)

```sql
CREATE TABLE CATEGORY (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE BRAND (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE PRODUCT (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    PRICE INT NOT NULL,
    BRAND_ID BIGINT NOT NULL,
    CATEGORY_ID BIGINT NOT NULL,
    FOREIGN KEY (BRAND_ID) REFERENCES BRAND(ID),
    FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY(ID)
);
```

### Sample Data (`data.sql`)

```sql
INSERT INTO CATEGORY (NAME) VALUES ('상의'), ('아우터'), ('바지');
INSERT INTO BRAND (NAME) VALUES ('A'), ('B'), ('C');
INSERT INTO PRODUCT (NAME, PRICE, BRAND_ID, CATEGORY_ID) VALUES
    ('상품1', 1000, 1, 1),
    ('상품2', 2000, 2, 2),
    ('상품3', 3000, 3, 3);
```

---

## 추가 정보

### Swagger 설정

- Swagger UI 경로: `/swagger-ui/index.html`
- API Docs 경로: `/api-docs`

### API 테스트

Swagger UI를 통해 API 테스트 가능.

---

## 개발자 노트

본 프로젝트는 무신사 과제로 설계되었습니다. 최신 기술 스택을 적극적으로 활용하여 고성능, 확장성, 테스트 가능성을 고려한 프로젝트입니다. 제공된 모든 기능은 TDD(Test-Driven Development)를 통해 검증되었습니다.

