package thatline.localup.tourapi

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import thatline.localup.tourapi.restclient.TourApiRestClient

@SpringBootTest
@ActiveProfiles("local")
@TestPropertySource(properties = ["logging.level.thatline.localup.tourapi.restclient.TourApiRestClient=DEBUG"])
class DataLabServiceTests {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    @Autowired
    lateinit var restClient: TourApiRestClient

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("한국관광공사_관광빅데이터 정보서비스_GW: 기초 지자체 지역방문자수 집계 데이터 정보 조회")
    fun runDataLabServiceLocgoRegnVisitrDDList() {
        // 지난 달, 같은 일자까지만 유효하게 처리
        val response = restClient.dataLabServiceLocgoRegnVisitrDDList(
            pageNo = 1,
            numOfRows = 10,
            startYmd = "20250723",
            endYmd = "20250723"
        )

        val responseString = objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(response)

        logger.info("\n{}", responseString)
    }
}
