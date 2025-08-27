# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요
LocalUp은 Spring Boot 3.5.0 기반의 Kotlin 백엔드 애플리케이션으로, 한국관광공사 API와 기상청 API를 활용하여 지역 사업자들을 위한 관광 정보 분석 서비스를 제공합니다.

## 기술 스택
- **언어**: Kotlin 1.9.25
- **프레임워크**: Spring Boot 3.5.0
- **데이터베이스**: MongoDB 8 (메인), Redis 8 (캐시/세션), H2 (개발/테스트)
- **빌드 도구**: Gradle (Kotlin DSL)
- **Java 버전**: 17

## 주요 명령어

### 로컬 환경 설정
```bash
# Docker 컨테이너 실행 (MongoDB, Redis)
cd docker/local
docker-compose -p localup_local up -d
```

### 애플리케이션 실행
```bash
./gradlew bootRun
```

### 테스트 실행
```bash
./gradlew test
```

### 빌드
```bash
./gradlew build
```

### 코드 스타일 검사
```bash
./gradlew ktlintCheck
```

## 아키텍처 개요

### 계층형 아키텍처
```
Controller → Service → Repository → Entity
    ↓           ↓          ↓          ↓
   REST    Business     Data      Database
  Layer      Logic     Access      Models
```

### 모듈 구조
- `auth/`: JWT 기반 인증 및 토큰 관리
- `user/`: 사용자 및 비즈니스 프로필 관리
- `common/`: 공통 설정, 유틸리티, 예외 처리
- `localup/`: 관광 정보 분석 핵심 비즈니스 로직
- `tourapi/`: 한국관광공사 5개 API 연동
- `etcapi/`: 기상청 API 등 외부 API 연동
- `dashboard/`: 대시보드 및 분석 리포트
- `storage/`: 파일 업로드/다운로드
- `chatgpt/`: OpenAI GPT 모델 연동
- `admin/`: 관리자 기능

### 핵심 패턴

#### 1. BaseResponse 패턴
모든 API 응답은 통일된 형식으로 래핑:
```kotlin
BaseResponse(code: String, message: String, data: T?)
```

#### 2. 다중 데이터베이스 지원
- **MongoDB**: 비즈니스 데이터 (users, businesses, files, chats)
- **Redis**: 토큰, 이메일 인증 코드, API 할당량 캐싱
- JPA와 MongoDB 엔티티를 별도 패키지로 관리

#### 3. 보안 아키텍처
- JWT 쿠키 기반 인증 (`AuthenticationFilter`)
- 커스텀 어노테이션: `@RequireUser`, `@RequireMaster`
- Kakao OAuth 소셜 로그인 지원

#### 4. API 할당량 관리
- `@OpenApiQuota` 어노테이션으로 외부 API 호출 추적
- `OpenApiQuotaAspect`로 일/월별 할당량 관리

#### 5. 예외 처리
- `GlobalExceptionHandler`: 전역 예외 처리
- 도메인별 예외 핸들러 (예: `TourApiExceptionHandler`)
- 커스텀 비즈니스 예외 정의

### 외부 API 연동

#### 한국관광공사 API
1. **KorService2**: 지역코드, 관광지 정보, 축제/행사
2. **TarRlteTarService1**: 연관 관광지 추천
3. **LocgoHubTarService1**: 지자체 관광 데이터
4. **TatsCnctrRateService**: 관광객 밀집도 예측
5. **DataLabService**: 방문객 통계 빅데이터

#### 기상청 API
- **VilageFcstInfoService_2.0**: 단기예보 조회

### 환경 설정
- `application.yaml`: 기본 설정 및 API 경로 정의
- `application-local.yaml`: 로컬 환경 설정 (DB 연결, API 키)
- `.env`: API 키 관리 (예: `KOR_SERVICE2_KEY`)
- Spring Profiles: `local` 프로파일 기본 활성화

### 개발 시 주의사항
- RestClient를 사용한 일관된 HTTP 통신
- 환경변수로 모든 API 키 관리
- MongoDB 커맨드 모니터링 및 로깅 활성화
- Redis 서비스별 전용 Service 클래스 사용
- AOP 기반 API 호출 모니터링