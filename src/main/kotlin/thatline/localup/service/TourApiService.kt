package thatline.localup.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder
import thatline.localup.dto.tourApi.TatsCnctrRatedListResponse
import thatline.localup.property.TourApiProperty
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class TourApiService(
    private val tourApiProperty: TourApiProperty,
    private val restClient: RestClient,
) {
    fun tatsCnctrRatedList(
        pageNo: Long,
        numOfRows: Long,
        areaCd: String,
        signguCd: String,
        tAtsNm: String,
    ): TatsCnctrRatedListResponse? {
        val fromUri = URI.create(
            "${tourApiProperty.baseUrl}${tourApiProperty.tatsCnctrRateService.firstPath}${tourApiProperty.tatsCnctrRateService.tatsCnctrRatedList.secondPath}"
        )

        val encodedTAtsNm = URLEncoder.encode(tAtsNm, StandardCharsets.UTF_8)

        val uri = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("serviceKey", tourApiProperty.tatsCnctrRateService.serviceKey)
            .queryParam("pageNo", pageNo)
            .queryParam("numOfRows", numOfRows)
            .queryParam("MobileOS", tourApiProperty.mobileOS)
            .queryParam("MobileApp", tourApiProperty.mobileApp)
            .queryParam("areaCd", areaCd)
            .queryParam("signguCd", signguCd)
            .queryParam("tAtsNm", encodedTAtsNm)
            .queryParam("_type", "JSON")
            .build(true)
            .toUri()

        val response = restClient.get()
            .uri(uri)
            .retrieve()
            .body(TatsCnctrRatedListResponse::class.java)

        return response
    }
}
