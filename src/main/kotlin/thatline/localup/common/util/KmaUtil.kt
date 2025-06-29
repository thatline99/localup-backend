package thatline.localup.common.util

import kotlin.math.*

// KMA(Korea Meteorological Administration, 기상청)
object KmaUtil {
    private const val RE = 6371.00877 // Earth radius (km)
    private const val GRID = 5.0 // Grid spacing (km)
    private const val SLAT1 = 30.0 // 1st standard parallel (deg)
    private const val SLAT2 = 60.0 // 2nd standard parallel (deg)
    private const val OLON = 126.0 // Reference longitude (deg)
    private const val OLAT = 38.0 // Reference latitude  (deg)
    private const val XO = 43.0 // Origin X (grid)
    private const val YO = 136.0 // Origin Y (grid)

    private const val DEGRAD = PI / 180.0

    /**
     * 위경도(WGS84)를 기상청 격자(nx, ny)로 변환
     *
     * @param latitude 위도
     * @param longitude 경도
     * @return (nx, ny) Pair<Int, Int>
     *
     * @see <a href="https://gist.github.com/fronteer-kr/14d7f779d52a21ac2f16">참고</a>
     */
    fun convertCoordinatesToGrid(latitude: Double, longitude: Double): Pair<Int, Int> {
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        val sn = ln(cos(slat1) / cos(slat2)) / ln(
            tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5)
        )

        val sf = tan(PI * 0.25 + slat1 * 0.5).pow(sn) * cos(slat1) / sn

        val ro = re * sf / tan(PI * 0.25 + olat * 0.5).pow(sn)

        val ra = re * sf / tan(PI * 0.25 + latitude * DEGRAD * 0.5).pow(sn)

        var theta = longitude * DEGRAD - olon
        if (theta > PI) theta -= 2.0 * PI
        if (theta < -PI) theta += 2.0 * PI
        theta *= sn

        val nx = floor(ra * sin(theta) + XO + 0.5).toInt()
        val ny = floor(ro - ra * cos(theta) + YO + 0.5).toInt()

        return nx to ny
    }
}
