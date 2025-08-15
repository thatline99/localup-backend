package thatline.localup.user.dto

import thatline.localup.user.entity.mongodb.CustomerSegment

data class FindBusinessDto(
    val name: String,
    val zipCode: String,
    val address: String,
    val addressDetail: String?,
    val latitude: Double,
    val longitude: Double,
    val type: String,
    val item: String,
    val averageOrderAmount: Double,
    val seatCount: Int,
    val customerSegments: Set<CustomerSegment>,
)
