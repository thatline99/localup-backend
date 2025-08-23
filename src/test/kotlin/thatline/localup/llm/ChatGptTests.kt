package thatline.localup.llm

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import thatline.localup.chatgpt.restclient.ChatGptRestClient
import thatline.localup.dashboard.service.DashboardFacade
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("local")
class ChatGptTests {
    @Autowired
    lateinit var chatGptRestClient: ChatGptRestClient

    @Autowired
    lateinit var dashboardFacade: DashboardFacade

    @Test
    @DisplayName("ChatGPT: 관광 데이터 분석 및 조언 실행 확인")
    fun runAnalyzeAndAdviseTourismData() {
        val userId = "689eb417e295ca9144b875a0"

        val foundShortTermForecast = dashboardFacade.findShortTermForecast(
            userId = userId,
        )

        val foundVisitorStatistics = dashboardFacade.findVisitorStatistics(
            userId = userId,
            startDate = LocalDate.of(2024, 8, 1),
            endDate = LocalDate.of(2024, 8, 31),
        )

        val dashboardOverview = dashboardFacade.getDashboardOverview(
            userId = userId
        )

        chatGptRestClient.analyzeAndAdviseTourismData(
            "오늘/내일/모레 날씨 예보: $foundShortTermForecast" +
                    "작년 같은 월 방문자(현지인/외지인/외국인) 수: $foundVisitorStatistics" +
                    "지난 달 관광지 랭킹 | 메인 이벤트 | 진행 중 또는 진행 예정 이벤트: $dashboardOverview"
        )

        /**
         * 응답 예시
         *
         * 1. 오늘, 내일, 모레 날씨가 더운 편이므로 시원한 음료 및 아이스크림 메뉴 확대 필요. 특히, 아이스 음료와 디저트를 프로모션하여 방문객 유치해야 함.
         * 2. 작년 같은 월 방문자 수를 고려할 때, 현지인 방문이 많으므로, 지역민을 위한 할인 이벤트나 특별 메뉴를 제공해야 함.
         * 3. 오늘 광복 80년 기념 빛 축제와 싱크 넥스트 25와 같은 주요 이벤트가 진행 중이므로, 카페 홍보를 위해 이벤트 장소 인근에 광고물 부착 및 소셜 미디어 홍보 강화 필요.
         * 4. 외국인 관광객 비율이 낮으므로, 외국인 관광객을 위한 다국어 메뉴 제공 및 친절한 서비스 교육 필요.
         * 5. 내일과 모레는 날씨가 비 예보가 있어 비 오는 날 특선 메뉴나 비 오는 날에 맞는 아늑한 분위기를 조성하여 고객 유입을 늘려야 함.
         * 6. 지난 달 관광지 랭킹을 참조하여 인근 인기 관광지 방문객이 많이 찾는 시간대에 맞춰 운영 시간을 조정하고, 특별 프로모션을 계획하는 것이 권장됨.
         * 7. 주말 방문객 수를 고려하여, 전통문화행사와 같은 지역 행사와 연계하여 특별 행사 진행 시 고객 유치 가능성을 높여야 함.
         * 8. 카페 내외부 인테리어를 여름 분위기에 맞게 꾸미고, 쾌적한 공간 제공으로 고객의 체류 시간을 늘려야 함.
         * 9. 소셜 미디어 및 지역 커뮤니티와의 협력을 통해 이벤트와 특별 메뉴를 홍보하여 인지도를 높일 것을 권장함.
         * 10. 매출 증대를 위해 전반적인 서비스 품질 개선과 고객 피드백 수집 및 반영 시스템을 마련해야 함.
         */
    }
}
