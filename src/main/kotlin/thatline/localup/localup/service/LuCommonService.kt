package thatline.localup.localup.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import thatline.localup.localup.response.dto.Area
import thatline.localup.localup.response.dto.Sigungu
import thatline.localup.tourapi.restclient.TourApiRestClient

@Service
class LuCommonService(
    private val tourApiRestClient: TourApiRestClient,
    private val objectMapper: ObjectMapper,
) {
    fun searchAreas(): List<Area> {
        val totalCount = tourApiRestClient.ldongCode2(
            pageNo = 1,
            numOfRows = 1,
        ).response.body.totalCount

        val items = tourApiRestClient.ldongCode2(
            pageNo = 1,
            numOfRows = totalCount,
        ).response.body.getItems(objectMapper)

        return items.map { Area(it.code, it.name) }
    }

    fun searchSigungus(
        areaCode: String,
    ): List<Sigungu> {
        // 일반적으로는 areaCode + sigunguCode 조합을 사용하지만,
        // 세종특별자치시(36110)는 하위 시군구가 없으므로 단건으로 직접 반환함
        if (areaCode == "36110") {
            return listOf(Sigungu(code = "36110", name = "세종특별자치시"))
        }

        val totalCount = tourApiRestClient.ldongCode2(
            pageNo = 1,
            numOfRows = 1,
            lDongRegnCd = areaCode
        ).response.body.totalCount

        val items = tourApiRestClient.ldongCode2(
            pageNo = 1,
            numOfRows = totalCount,
            lDongRegnCd = areaCode
        ).response.body.getItems(objectMapper)

        return items.map { Sigungu("$areaCode${it.code}", it.name) }
    }
}
