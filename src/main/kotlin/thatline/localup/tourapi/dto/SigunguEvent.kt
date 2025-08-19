package thatline.localup.tourapi.dto

data class SigunguEvent(
    val contentTypeId: String,
    val contentId: String,
    val title: String,
    val zipCode: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val originalImageUrl: String,
    val thumbnailImageUrl: String,
    val telephone: String,
)
