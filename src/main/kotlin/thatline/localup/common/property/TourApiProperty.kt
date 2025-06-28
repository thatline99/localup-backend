package thatline.localup.common.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "tour-api")
@Validated
class TourApiProperty(
    val baseUrl: String,
    val mobileOS: String,
    val mobileApp: String,
    val korService2: KorService2,
    val tarRlteTarService: TarRlteTarService,
    val locgoHubTarService: LocgoHubTarService,
    val tatsCnctrRateService: TatsCnctrRateService,
    val dataLabService: DataLabService,
) {
    /**
     * 한국관광공사_국문 관광정보 서비스_GW
     *
     * @see <a href="https://www.data.go.kr/data/15101578/openapi.do">공공데이터포털 API 문서</a>
     */
    data class KorService2(
        val firstPath: String,
        val serviceKey: String,
        val areaCode2: AreaCode2,
    ) {
        data class AreaCode2(
            val secondPath: String,
            val responseType: String,
        )
    }


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

    /**
     * 한국관광공사_기초지자체 중심 관광지 정보
     *
     * @see <a href="https://www.data.go.kr/data/15128559/openapi.do">공공데이터포털 API 문서</a>
     */
    data class LocgoHubTarService(
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
