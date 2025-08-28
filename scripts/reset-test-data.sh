#!/bin/bash

# LocalUp 테스트 데이터 초기화 스크립트
# MongoDB와 Redis의 모든 테스트 데이터를 삭제합니다.

set -e

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}╔════════════════════════════════════════════╗${NC}"
echo -e "${YELLOW}║     LocalUp 테스트 데이터 초기화 스크립트     ║${NC}"
echo -e "${YELLOW}╚════════════════════════════════════════════╝${NC}"
echo ""

# Docker 컨테이너 실행 확인
echo -e "${GREEN}▶ Docker 컨테이너 상태 확인...${NC}"
MONGO_CONTAINER="mongodb-localup_local"
REDIS_CONTAINER="redis-localup_local"

if ! docker ps | grep -q $MONGO_CONTAINER; then
    echo -e "${RED}✗ MongoDB 컨테이너가 실행 중이 아닙니다.${NC}"
    echo "  다음 명령어로 컨테이너를 시작하세요:"
    echo "  cd ../docker/local && docker-compose -p localup_local up -d"
    exit 1
fi

if ! docker ps | grep -q $REDIS_CONTAINER; then
    echo -e "${RED}✗ Redis 컨테이너가 실행 중이 아닙니다.${NC}"
    echo "  다음 명령어로 컨테이너를 시작하세요:"
    echo "  cd ../docker/local && docker-compose -p localup_local up -d"
    exit 1
fi

echo -e "${GREEN}✓ 컨테이너가 정상적으로 실행 중입니다.${NC}"
echo ""

# 사용자 확인
echo -e "${YELLOW}⚠️  경고: 이 작업은 모든 테스트 데이터를 삭제합니다!${NC}"
echo -e "다음 데이터가 삭제됩니다:"
echo "  • MongoDB: localup 데이터베이스의 모든 컬렉션"
echo "  • Redis: 모든 키-값 데이터"
echo ""
read -p "정말로 계속하시겠습니까? (y/N): " -n 1 -r
echo ""

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}작업이 취소되었습니다.${NC}"
    exit 0
fi

echo ""
echo -e "${GREEN}▶ MongoDB 데이터 삭제 시작...${NC}"

# MongoDB 데이터 삭제
docker exec -i $MONGO_CONTAINER mongosh -u root -p root --authenticationDatabase admin <<EOF
use localup;

// 컬렉션 목록 확인
print("현재 컬렉션 목록:");
db.getCollectionNames().forEach(function(collection) {
    var count = db[collection].countDocuments();
    print("  - " + collection + ": " + count + "개 문서");
});

print("");
print("데이터 삭제 중...");

// 모든 컬렉션 삭제
db.getCollectionNames().forEach(function(collection) {
    if (collection !== "system.indexes") {
        db[collection].drop();
        print("  ✓ " + collection + " 컬렉션 삭제됨");
    }
});

print("");
print("MongoDB 초기화 완료!");
EOF

echo ""
echo -e "${GREEN}✓ MongoDB 데이터 삭제 완료${NC}"
echo ""

echo -e "${GREEN}▶ Redis 데이터 삭제 시작...${NC}"

# Redis 데이터 삭제
docker exec -i $REDIS_CONTAINER redis-cli -a root <<EOF
INFO keyspace
FLUSHALL
INFO keyspace
EOF

echo ""
echo -e "${GREEN}✓ Redis 데이터 삭제 완료${NC}"
echo ""

# 완료 메시지
echo -e "${GREEN}╔════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║        테스트 데이터 초기화 완료!           ║${NC}"
echo -e "${GREEN}╚════════════════════════════════════════════╝${NC}"
echo ""
echo "다음 작업을 수행할 수 있습니다:"
echo "  1. 백엔드 서버 재시작: ./gradlew bootRun"
echo "  2. 프론트엔드 서버 재시작: pnpm run dev"
echo "  3. 새로운 테스트 계정 생성"
echo ""