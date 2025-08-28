#!/bin/bash

# LocalUp 데이터베이스 상태 확인 스크립트
# MongoDB와 Redis의 현재 데이터 상태를 표시합니다.

set -e

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

echo -e "${CYAN}╔════════════════════════════════════════════╗${NC}"
echo -e "${CYAN}║      LocalUp 데이터베이스 상태 확인        ║${NC}"
echo -e "${CYAN}╚════════════════════════════════════════════╝${NC}"
echo ""

# Docker 컨테이너 실행 확인
MONGO_CONTAINER="mongodb-localup_local"
REDIS_CONTAINER="redis-localup_local"

echo -e "${GREEN}▶ MongoDB 상태${NC}"
echo ""

if docker ps | grep -q $MONGO_CONTAINER; then
    docker exec -i $MONGO_CONTAINER mongosh -u root -p root --authenticationDatabase admin --quiet <<'EOF'
use localup;

print("데이터베이스: localup");
print("=====================================");

// 컬렉션별 통계
var collections = db.getCollectionNames();
var totalDocs = 0;

collections.forEach(function(collName) {
    if (collName !== "system.indexes") {
        var count = db[collName].countDocuments();
        totalDocs += count;
        
        if (count > 0) {
            print("📁 " + collName + ": " + count + "개 문서");
            
            // 특별 컬렉션 상세 정보
            if (collName === "user") {
                var verified = db.user.countDocuments({isEmailVerified: true});
                var unverified = db.user.countDocuments({isEmailVerified: false});
                var withBusiness = db.user.countDocuments({businessId: {$ne: null}});
                var profileCompleted = db.user.countDocuments({isProfileCompleted: true});
                print("   ├─ 이메일 인증 완료: " + verified + "명");
                print("   ├─ 이메일 인증 대기: " + unverified + "명");
                print("   ├─ 프로필 완성: " + profileCompleted + "명");
                print("   └─ 사업정보 등록: " + withBusiness + "명");
            }
            
            if (collName === "business") {
                var types = db.business.aggregate([
                    {$group: {_id: "$type", count: {$sum: 1}}}
                ]);
                print("   업종별 분포:");
                types.forEach(function(type) {
                    print("   └─ " + type._id + ": " + type.count + "개");
                });
            }
            
            if (collName === "loginHistory") {
                var recent = db.loginHistory.find({loginSuccess: true})
                    .sort({createdDate: -1}).limit(1).toArray();
                if (recent.length > 0) {
                    print("   └─ 최근 로그인: " + recent[0].email + 
                          " (" + recent[0].createdDate.toLocaleString() + ")");
                }
            }
        }
    }
});

print("");
print("총 문서 수: " + totalDocs + "개");
EOF
else
    echo -e "${RED}✗ MongoDB 컨테이너가 실행 중이 아닙니다.${NC}"
fi

echo ""
echo -e "${GREEN}▶ Redis 상태${NC}"
echo ""

if docker ps | grep -q $REDIS_CONTAINER; then
    docker exec -i $REDIS_CONTAINER redis-cli -a root --no-auth-warning <<'EOF' | grep -v "^$"
INFO keyspace
DBSIZE
EOF
    
    echo ""
    echo "활성 토큰 목록:"
    docker exec -i $REDIS_CONTAINER redis-cli -a root --no-auth-warning <<'EOF' | head -20
KEYS accessToken:*
EOF
else
    echo -e "${RED}✗ Redis 컨테이너가 실행 중이 아닙니다.${NC}"
fi

echo ""
echo -e "${CYAN}=====================================

${NC}"
echo "실행 가능한 스크립트:"
echo "  • ./reset-test-data.sh         : 모든 테스트 데이터 삭제"
echo "  • ./reset-specific-user.sh     : 특정 사용자 데이터 삭제"
echo "  • ./show-db-status.sh          : 현재 상태 확인"
echo ""