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
class KorService2Tests {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var restClient: TourApiRestClient

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("한국관광공사_국문 관광정보 서비스_GW, 지역 기반 관광 정보 조회 테스트")
    fun runKorService2AreaBasedList2() {
        val response = restClient.korService2AreaBasedList2()

        val responseString = objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(response)

        log.info("\n{}", responseString)
    }

    @Test
    @DisplayName("한국관광공사_국문 관광정보 서비스_GW: 행사 정보 조회 실행 테스트")
    fun runKorService2SearchFestival2() {
        val response = restClient.korService2SearchFestival2(
            eventStartDate = "20250801"
        )

        val responseString = objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(response)

        log.info("\n{}", responseString)
    }

    @Test
    @DisplayName("한국관광공사_국문 관광정보 서비스_GW,  소개 정보 조회 15 (축제/공연/행사) 실행 테스트")
    fun runKorService2DetailIntro215() {
        val response = restClient.korService2DetailIntro215(
            contentId = "3391612",
            contentTypeId = "15",
        )

        val responseString = objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(response)

        log.info("\n{}", responseString)
    }
}
