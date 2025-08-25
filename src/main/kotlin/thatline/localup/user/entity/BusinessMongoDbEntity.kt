package thatline.localup.user.entity

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import thatline.localup.common.entity.BaseMongoDbEntity
import java.time.LocalDateTime

@Document(collection = "business")
class BusinessMongoDbEntity(
    id: String = ObjectId().toHexString(),

    createdDate: LocalDateTime = LocalDateTime.now(),

    lastModifiedDate: LocalDateTime = createdDate,

    // 상호명
    val name: String,

    // 시군구 코드
    val sigunguCode: String,

    // 사업장(단체) 소재지, 우편번호
    val zipCode: String,

    // 사업장(단체) 소재지, 주소
    val address: String,

    // 사업장(단체) 소재지, 주소 상세
    val addressDetail: String?,

    // 사업장(단체) 소재지, 위도
    val latitude: Double,

    // 사업장(단체) 소재지, 경도
    val longitude: Double,

    // 업종, 주업태
    val type: String,

    // 업종, 주종목
    val item: String,

    // 평균 일간 고객 수
    val averageDailyCustomerCount: Int,

    // 평균 객단가
    val averageOrderAmount: Double,

    // 좌석 수
    val seatCount: Int,

    // 주요 고객층 (선택)
    val customerSegments: Set<CustomerSegment>,

    // 설명 (선택)
    val description: String?,
) : BaseMongoDbEntity(id, createdDate, lastModifiedDate) {
    fun update(
        name: String = this.name,
        sigunguCode: String = this.sigunguCode,
        zipCode: String = this.zipCode,
        address: String = this.address,
        addressDetail: String? = this.addressDetail,
        latitude: Double = this.latitude,
        longitude: Double = this.longitude,
        type: String = this.type,
        item: String = this.item,
        averageDailyCustomerCount: Int = this.averageDailyCustomerCount,
        averageOrderAmount: Double = this.averageOrderAmount,
        seatCount: Int = this.seatCount,
        customerSegments: Set<CustomerSegment> = this.customerSegments,
        description: String? = this.description,
    ): BusinessMongoDbEntity {
        return BusinessMongoDbEntity(
            id = this.id,
            createdDate = this.createdDate,
            lastModifiedDate = LocalDateTime.now(),
            name = name,
            sigunguCode = sigunguCode,
            zipCode = zipCode,
            address = address,
            addressDetail = addressDetail,
            latitude = latitude,
            longitude = longitude,
            type = type,
            item = item,
            averageDailyCustomerCount = averageDailyCustomerCount,
            averageOrderAmount = averageOrderAmount,
            seatCount = seatCount,
            customerSegments = customerSegments.toSet(),
            description = description
        )
    }
}
