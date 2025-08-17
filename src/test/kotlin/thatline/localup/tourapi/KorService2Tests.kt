package thatline.localup.tourapi

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import thatline.localup.tourapi.restclient.TourApiRestClient

@SpringBootTest
@ActiveProfiles("local")
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
}
