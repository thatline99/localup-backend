package thatline.localup.user.request

data class UpdateAddressRequest(

    val zipCode: String,
    val address: String,
    val addressDetail: String?,
    val latitude: Double,
    val longitude: Double,
)
