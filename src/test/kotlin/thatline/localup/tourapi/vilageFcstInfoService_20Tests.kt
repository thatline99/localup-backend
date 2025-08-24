package thatline.localup.tourapi

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import thatline.localup.etcapi.restclient.EtcApiRestClient

@SpringBootTest
@ActiveProfiles("local")
@TestPropertySource(properties = ["logging.level.thatline.localup.tourapi.restclient.EtcApiRestClient=DEBUG"])
class vilageFcstInfoService_20Tests {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    @Autowired
    lateinit var restClient: EtcApiRestClient

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("기상청_단기예보 ((구) 동네예보) 조회서비스: 초단기실황조회")
    fun runVilageFcstInfoService_20GetUltraSrtNcst() {
        val response = restClient.getUltraSrtNcst(
            pageNo = 1,
            numOfRows = 10,
            baseDate = "20250821",
            baseTime = "1100",
            nx = 55,
            ny = 127,
        )

        val responseString = objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(response)

        logger.info("\n{}", responseString)
    }

    @Test
    @DisplayName("기상청_단기예보 ((구) 동네예보) 조회서비스: 단기예보조회")
    fun runVilageFcstInfoService_20GetVilageFcst() {
        val response = restClient.getVilageFcst(
            pageNo = 1,
            numOfRows = 10,
            baseDate = "20250822",
            baseTime = "0500",
            nx = 55,
            ny = 127,
        )

        val responseString = objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(response)

        logger.info("\n{}", responseString)
    }
}
