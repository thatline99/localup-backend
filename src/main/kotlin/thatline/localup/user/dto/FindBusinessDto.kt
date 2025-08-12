package thatline.localup.user.dto

data class FindBusinessDto(
    val name: String,
    val zipCode: String,
    val address: String,
    val addressDetail: String?,
    val latitude: Double,
    val longitude: Double,
    val type: String,
    val item: String,
)
