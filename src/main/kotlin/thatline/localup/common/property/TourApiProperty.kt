package thatline.localup.common.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "tour-api")
@Validated
class TourApiProperty(
    val baseUrl: String,
    val mobileOS: String,
    val mobileApp: String,
    val tarRlteTarService: TarRlteTarService,
    val tatsCnctrRateService: TatsCnctrRateService,
    val dataLabService: DataLabService,
) {
    /**
     * 한국관광공사_관광지별 연관 관광지 정보
     *
     * @see <a href="https://www.data.go.kr/data/15128560/openapi.do">공공데이터포털 API 문서</a>
     */
    data class TarRlteTarService(
        val firstPath: String,
        val serviceKey: String,
        val areaBasedList: AreaBasedList,
    ) {
        data class AreaBasedList(
            val secondPath: String,
            val responseType: String,
        )
    }

    data class TatsCnctrRateService(
        val firstPath: String,
        val serviceKey: String,
        val tatsCnctrRatedList: TatsCnctrRatedList,
    ) {
        data class TatsCnctrRatedList(
            val secondPath: String,
        )
    }

    data class DataLabService(
        val firstPath: String,
        val serviceKey: String,
        val metcoRegnVisitrDDList: MetcoRegnVisitrDDList,
        val locgoRegnVisitrDDList: LocgoRegnVisitrDDList,
    ) {
        data class MetcoRegnVisitrDDList(
            val secondPath: String,
        )

        data class LocgoRegnVisitrDDList(
            val secondPath: String,
        )
    }
}
