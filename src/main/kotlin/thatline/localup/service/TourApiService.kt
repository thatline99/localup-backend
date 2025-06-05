package thatline.localup.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder
import thatline.localup.dto.tourApi.TatsCnctrRatedResponse
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
        pageNo: Int = 1,
        numOfRows: Int = 30,
        areaCd: String,
        signguCd: String,
        tAtsNm: String,
    ): TatsCnctrRatedResponse? {
        val fromUri = URI.create(
            "${tourApiProperty.baseUrl}${tourApiProperty.tatsCnctrRateService.firstPath}${tourApiProperty.tatsCnctrRateService.tatsCnctrRatedList.secondPath}"
        )

        val encodedTAtsNm = URLEncoder.encode(tAtsNm, StandardCharsets.UTF_8)

        val uri = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("serviceKey", tourApiProperty.tatsCnctrRateService.serviceKey)
            .queryParam("pageNo", pageNo)
            .queryParam("numOfRows", numOfRows)
            .queryParam("MobileOS", "WEB")
            .queryParam("MobileApp", "LocalUp")
            .queryParam("areaCd", areaCd)
            .queryParam("signguCd", signguCd)
            .queryParam("tAtsNm", encodedTAtsNm)
            .queryParam("_type", "JSON")
            .build(true)
            .toUri()

        val response = restClient.get()
            .uri(uri)
            .retrieve()
            .body(TatsCnctrRatedResponse::class.java)

        return response
    }
}
