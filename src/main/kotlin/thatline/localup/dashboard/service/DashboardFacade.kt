package thatline.localup.dashboard.service

import org.springframework.stereotype.Service
import thatline.localup.etcapi.dto.WeatherInformation
import thatline.localup.etcapi.service.WeatherService
import thatline.localup.user.service.UserService

@Service
class DashboardFacade(
    private val userService: UserService,
    private val weatherService: WeatherService,
) {
    fun getDashboardOverview(userId: String): WeatherInformation {
        val foundUserBusinessDto = userService.findBusiness(userId)

        return weatherService.getThreeDayWeatherSummaries(foundUserBusinessDto.sigunguCode)
    }
}
