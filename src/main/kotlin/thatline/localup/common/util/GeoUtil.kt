package thatline.localup.common.util

import kotlin.math.*

object GeoUtil {
    // WGS84 타원체 기반 평균 반지름
    private const val EARTH_RADIUS_KILOMETER = 6371.0088

    fun distanceInKilometer(
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double,
    ): Double {
        val deltaLatitude = Math.toRadians(latitude2 - latitude1)
        val deltaLongitude = Math.toRadians(longitude2 - longitude1)

        val haversine = sin(deltaLatitude / 2).pow(2.0) +
                cos(Math.toRadians(latitude1)) * cos(Math.toRadians(latitude2)) *
                sin(deltaLongitude / 2).pow(2.0)

        val centralAngle = 2 * atan2(sqrt(haversine), sqrt(1 - haversine))

        return EARTH_RADIUS_KILOMETER * centralAngle
    }
}
