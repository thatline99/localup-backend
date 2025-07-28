# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요
LocalUp은 Spring Boot 3.5.0 기반의 Kotlin 백엔드 애플리케이션으로, 한국관광공사 API를 활용하여 관광 정보 서비스를 제공합니다.

## 기술 스택
- **언어**: Kotlin 1.9.25
- **프레임워크**: Spring Boot 3.5.0
- **데이터베이스**: MongoDB, H2 (JPA), Redis
- **빌드 도구**: Gradle (Kotlin DSL)
- **Java 버전**: 17

## 주요 명령어

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

### 로컬 환경 Docker 컨테이너 실행
```bash
cd docker/local
docker-compose -p localup_local up -d
```

## 프로젝트 구조

### 계층형 아키텍처
- **Controller**: REST API 엔드포인트 정의
- **Service**: 비즈니스 로직 처리
- **Repository**: 데이터 접근 계층 (JPA/MongoDB)
- **Entity**: 데이터베이스 엔티티 (JPA/MongoDB 분리)
- **DTO/Request/Response**: 데이터 전송 객체
- **RestClient**: 외부 API 통신

### 모듈별 구조
- `auth/`: 인증 및 토큰 관리
- `common/`: 공통 설정, 유틸리티, 예외 처리
- `user/`: 사용자 관리
- `localup/`: 관광 정보 서비스 핵심 로직
- `tourapi/`: 한국관광공사 API 연동
- `etcapi/`: 기상청 등 기타 외부 API 연동

### 핵심 패턴
1. **다중 데이터베이스 지원**: JPA와 MongoDB 엔티티를 별도로 관리
2. **BaseResponse 패턴**: 모든 API 응답을 통일된 형식으로 래핑
3. **GlobalExceptionHandler**: 전역 예외 처리
4. **Spring Security**: JWT 기반 인증 (AuthenticationFilter)
5. **Redis 캐싱**: 토큰 관리에 사용

### 외부 API 연동
- 한국관광공사 API들을 RestClient를 통해 호출
- 환경변수로 API 키 관리 (예: `${KOR_SERVICE2_KEY}`)
- 각 API별 전용 Request/Response DTO 정의

### 주요 설정
- `application.yaml`: 기본 설정 및 API 경로 정의
- `application-local.yaml`: 로컬 환경 설정 (DB 연결, API 키)
- Spring Profiles: `local` 프로파일 기본 활성화