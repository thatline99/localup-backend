#!/bin/bash

# LocalUp 특정 사용자 데이터 삭제 스크립트
# 특정 이메일 주소의 사용자 데이터만 삭제합니다.

set -e

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${YELLOW}╔════════════════════════════════════════════╗${NC}"
echo -e "${YELLOW}║     LocalUp 특정 사용자 데이터 삭제         ║${NC}"
echo -e "${YELLOW}╚════════════════════════════════════════════╝${NC}"
echo ""

# 파라미터 확인
if [ -z "$1" ]; then
    echo -e "${RED}사용법: $0 <이메일주소>${NC}"
    echo "예시: $0 test@example.com"
    exit 1
fi

EMAIL="$1"

# Docker 컨테이너 실행 확인
echo -e "${GREEN}▶ Docker 컨테이너 상태 확인...${NC}"
MONGO_CONTAINER="mongodb-localup_local"
REDIS_CONTAINER="redis-localup_local"

if ! docker ps | grep -q $MONGO_CONTAINER; then
    echo -e "${RED}✗ MongoDB 컨테이너가 실행 중이 아닙니다.${NC}"
    exit 1
fi

echo -e "${GREEN}✓ 컨테이너가 정상적으로 실행 중입니다.${NC}"
echo ""

# 사용자 확인
echo -e "${YELLOW}삭제할 사용자: ${BLUE}$EMAIL${NC}"
read -p "정말로 이 사용자의 데이터를 삭제하시겠습니까? (y/N): " -n 1 -r
echo ""

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}작업이 취소되었습니다.${NC}"
    exit 0
fi

echo ""
echo -e "${GREEN}▶ 사용자 데이터 삭제 시작...${NC}"

# MongoDB에서 사용자 데이터 삭제
docker exec -i $MONGO_CONTAINER mongosh -u root -p root --authenticationDatabase admin <<EOF
use localup;

// 사용자 조회
var user = db.user.findOne({email: "$EMAIL"});
if (user) {
    print("사용자 정보 찾음:");
    print("  - ID: " + user._id);
    print("  - 이름: " + (user.name || "없음"));
    print("  - 사업정보 ID: " + (user.businessId || "없음"));
    
    // 사업정보 삭제
    if (user.businessId) {
        var result = db.business.deleteOne({_id: ObjectId(user.businessId)});
        print("  ✓ 사업정보 삭제: " + result.deletedCount + "개");
    }
    
    // 로그인 기록 삭제
    var loginResult = db.loginHistory.deleteMany({email: "$EMAIL"});
    print("  ✓ 로그인 기록 삭제: " + loginResult.deletedCount + "개");
    
    // 채팅 세션 삭제
    var chatResult = db.chatSession.deleteMany({userId: user._id.toString()});
    print("  ✓ 채팅 세션 삭제: " + chatResult.deletedCount + "개");
    
    // 사용자 삭제
    var userResult = db.user.deleteOne({_id: user._id});
    print("  ✓ 사용자 삭제: " + userResult.deletedCount + "개");
    
    print("");
    print("사용자 데이터 삭제 완료!");
} else {
    print("해당 이메일의 사용자를 찾을 수 없습니다: $EMAIL");
}
EOF

echo ""
echo -e "${GREEN}✓ 사용자 데이터 삭제 완료${NC}"
echo ""