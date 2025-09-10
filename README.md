# 로컬업 백엔드

로컬업 : 자영업자를 위한 AI 솔루션 서비스 백엔드

## 시작하기

### 필수 요구사항

- Java 17 이상
- Docker & Docker Compose
- Gradle 8.x

### 설치 및 실행

#### 1. Docker 컨테이너 실행 (MongoDB, Redis)

```bash
cd docker/local
docker-compose -p localup_local up -d
```

#### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

또는 IDE에서 `src/main/kotlin/thatline/localup/LocalupApplication.kt` 직접 실행

개발 서버가 실행되면 API는 [http://localhost:8080](http://localhost:8080)에서 접근 가능합니다.

### 기타 명령어

```bash
./gradlew build     # 프로젝트 빌드
./gradlew test      # 테스트 실행
./gradlew clean     # 빌드 파일 정리
```

## 프로젝트 구조

```
src/
├── main/
│   ├── kotlin/thatline/localup/
│   │   ├── LocalupApplication.kt    # 애플리케이션 진입점
│   │   ├── admin/                   # 관리자 기능
│   │   │   └── controller/
│   │   ├── auth/                    # 인증 및 토큰 관리
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   ├── common/                  # 공통 설정 및 유틸리티
│   │   │   ├── annotation/          # 커스텀 어노테이션
│   │   │   ├── configuration/       # Spring 설정
│   │   │   ├── constant/            # 상수 정의
│   │   │   ├── exception/           # 전역 예외 처리
│   │   │   ├── filter/              # 필터 (인증 등)
│   │   │   ├── property/            # 설정 프로퍼티
│   │   │   └── util/                # 유틸리티 클래스
│   │   ├── dashboard/               # 대시보드 기능
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   └── service/
│   │   ├── etcapi/                  # 기타 외부 API 연동 (기상청 등)
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── restclient/
│   │   │   └── service/
│   │   ├── localup/                 # 로컬업 핵심 비즈니스 로직
│   │   │   ├── controller/
│   │   │   ├── request/
│   │   │   ├── response/
│   │   │   └── service/
│   │   ├── storage/                 # 파일 저장소 관리
│   │   │   ├── controller/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   ├── tourapi/                 # 한국관광공사 API 연동
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── request/
│   │   │   ├── response/
│   │   │   ├── restclient/
│   │   │   └── service/
│   │   └── user/                    # 사용자 관리
│   │       ├── controller/
│   │       ├── entity/
│   │       ├── repository/
│   │       └── service/
│   └── resources/
│       ├── application.yaml         # 기본 설정
│       └── application-local.yaml   # 로컬 환경 설정
└── test/                           # 테스트 코드
```

## 주요 API 엔드포인트

### 인증 API (`/api/auth`)

- **POST** `/api/auth/sign-up` - 회원가입
- **POST** `/api/auth/sign-in` - 로그인
- **POST** `/api/auth/sign-out` - 로그아웃

### 사용자 API (`/api/users`)

- **GET** `/api/users/business` - 사업체 정보 조회
- **POST** `/api/users/business` - 사업체 등록
- **PATCH** `/api/users/business` - 사업체 정보 수정

### 대시보드 API (`/api/dashboard`)

- **GET** `/api/dashboard` - 대시보드 개요 조회

### 로컬업 API (`/api/local-up`)

- **GET** `/api/local-up/tour-api-areas` - 관광 API 지역 조회
- **GET** `/api/local-up/areas` - 지역 목록 조회
- **GET** `/api/local-up/sigungus` - 시군구 목록 조회
- **GET** `/api/local-up/weathers` - 날씨 정보 조회
- **GET** `/api/local-up/tatsCnctrRatedList/tourist-attractions` - 관광지 집중도 조회
- **GET** `/api/local-up/tatsCnctrRatedList/tourist-attraction-last-30-days` - 최근 30일 관광지 집중도
- **GET** `/api/local-up/metcoRegnVisitrDDList` - 광역시도 방문자 통계
- **GET** `/api/local-up/locgoRegnVisitrDDList` - 시군구 방문자 통계

### 한국관광공사 API (`/api/tour-api`)

- **GET** `/api/tour-api/areaCode2` - 지역 코드 조회
- **GET** `/api/tour-api/ldongCode2` - 법정동 코드 조회
- **GET** `/api/tour-api/areaBasedList` - 지역 기반 관광정보 목록
- **GET** `/api/tour-api/areaBasedList2` - 지역 기반 관광정보 목록 (확장)
- **GET** `/api/tour-api/tatsCnctrRatedList` - 관광지 집중도 순위
- **GET** `/api/tour-api/metcoRegnVisitrDDList` - 광역시도 방문자 일별 목록
- **GET** `/api/tour-api/locgoRegnVisitrDDList` - 시군구 방문자 일별 목록

### 기타 외부 API (`/api/etc-api`)

- **GET** `/api/etc-api/getUltraSrtNcst` - 초단기 실황 조회 (기상청)
- **GET** `/api/etc-api/getFcstVersion` - 예보 버전 조회 (기상청)

### 파일 저장소 API (`/api/storage`)

- **POST** `/api/storage/upload` - 파일 업로드
- **GET** `/api/storage/files` - 파일 목록 조회
- **DELETE** `/api/storage/files/{fileId}` - 파일 삭제
- **GET** `/api/storage/files/{fileId}/download` - 파일 다운로드

### 관리자 API (`/api/admin`)

- **GET** `/api/admin/master` - 마스터 권한 확인 (RequireMaster)
- **GET** `/api/admin/user` - 사용자 권한 확인 (RequireUser)

### 상태 확인 API

- **GET** `/api/health` - 서버 상태 확인

## 기술 스택

### 프레임워크 및 라이브러리

- **Spring Boot 3.5.0** - 애플리케이션 프레임워크
- **Kotlin 1.9.25** - 프로그래밍 언어
- **Spring Security** - 보안 및 인증
- **Spring Data MongoDB** - MongoDB 연동
- **Spring Data Redis** - Redis 캐시 연동
- **Spring Validation** - 데이터 유효성 검증
- **Jackson Kotlin Module** - JSON 직렬화/역직렬화

### 데이터베이스

- **MongoDB 8** - 메인 데이터베이스 (사용자, 사업체, 파일 정보 등)
- **Redis 8** - 캐시 및 토큰 저장소

### 개발 도구

- **Gradle (Kotlin DSL)** - 빌드 도구
- **Spring Boot DevTools** - 개발 편의 도구
- **JUnit 5** - 테스트 프레임워크

### 외부 API 연동

- **한국관광공사 API** - 관광 정보 서비스
- **기상청 API** - 날씨 정보 서비스

## 주요 기능

### 인증 및 보안
- JWT 기반 토큰 인증
- Spring Security를 통한 보안 설정
- Redis를 활용한 토큰 관리
- 커스텀 어노테이션 기반 권한 관리 (@RequireUser, @RequireMaster)

### 데이터 관리
- MongoDB를 활용한 비정형 데이터 저장
- BaseResponse 패턴을 통한 통일된 API 응답
- 전역 예외 처리 (GlobalExceptionHandler)
- 캐싱 전략 구현 (Redis)

### 외부 서비스 연동
- RestClient를 통한 외부 API 호출
- 한국관광공사 관광정보 실시간 연동
- 기상청 날씨 정보 실시간 조회
- 환경변수 기반 API 키 관리

### 비즈니스 로직
- 대시보드 통계 및 분석 제공
- 지역별 관광지 정보 집계
- 방문자 통계 분석
- 사업체 정보 관리
- 파일 업로드/다운로드 기능

## 환경 설정

### 로컬 개발 환경
- Spring Profile: `local`
- MongoDB: localhost:27017
- Redis: localhost:6379
- API Keys: `application-local.yaml`에서 환경변수로 관리

### 필수 환경변수 : .env 파일 필수 
- `KOR_SERVICE2_KEY`: 한국관광공사 API 키
- `KMA_API_KEY`: 기상청 API 키
- MongoDB/Redis 연결 정보
