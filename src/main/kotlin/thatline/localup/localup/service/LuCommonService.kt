package thatline.localup.localup.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import thatline.localup.localup.response.dto.Area
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
            1,
            totalCount,
        ).response.body.getItems(objectMapper)

        return items.map { Area(it.code, it.name) }
    }
}
