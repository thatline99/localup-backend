package thatline.localup.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import kotlin.test.Test

@SpringBootTest
class LocalUpServiceIntegrationTest {

    @Autowired
    lateinit var localUpService: LocalUpService

    @Test
    fun testMetcoRegnVisitrDDListFetch() {
        val startDate = LocalDate.of(2025, 1, 3)
        val endDate = LocalDate.of(2025, 1, 3)

        localUpService.test(startDate, endDate)
    }
}