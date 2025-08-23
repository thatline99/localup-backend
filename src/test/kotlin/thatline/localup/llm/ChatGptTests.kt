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

        chatGptRestClient.analyzeAndAdviseTourismData(foundShortTermForecast.toString() + foundVisitorStatistics.toString())
    }
}
