package thatline.localup.tourapi.dto

data class LastMonthlyTouristAttractionRanking(
    val rank: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val category: String,
    val subCategory: String,
)
