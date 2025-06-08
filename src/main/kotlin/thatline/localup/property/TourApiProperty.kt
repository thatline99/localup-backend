package thatline.localup.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "tour-api")
@Validated
class TourApiProperty(
    val baseUrl: String,
    val mobileOS: String,
    val mobileApp: String,
    val tatsCnctrRateService: TatsCnctrRateService,
    val dataLabService: DataLabService,
) {
    data class TatsCnctrRateService(
        val firstPath: String,
        val serviceKey: String,
        var tatsCnctrRatedList: TatsCnctrRatedList,
    ) {
        data class TatsCnctrRatedList(
            var secondPath: String,
        )
    }

    data class DataLabService(
        val firstPath: String,
        val serviceKey: String,
        var metcoRegnVisitrDDList: MetcoRegnVisitrDDList,
    ) {
        data class MetcoRegnVisitrDDList(
            var secondPath: String,
        )
    }
}
