package thatline.localup.common.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "etc-api")
@Validated
class EtcApiProperty(
    val kma: Kma,
) {
    data class Kma(
        val baseUrl: String,
        val vilageFcstInfoService: VilageFcstInfoService,
    ) {
        data class VilageFcstInfoService(
            val firstPath: String,
            val serviceKey: String,
            val getUltraSrtNcst: GetUltraSrtNcst,
            val getFcstVersion: GetFcstVersion,
        ) {
            data class GetUltraSrtNcst(
                val secondPath: String,
                val dataType: String,
            )

            data class GetFcstVersion(
                val secondPath: String,
                val dataType: String,
            )
        }
    }
}
