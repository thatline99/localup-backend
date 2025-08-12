package thatline.localup.user.entity.mongodb

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import thatline.localup.common.entity.mongodb.BaseMongoDbEntity
import java.time.LocalDateTime

@Document(collection = "business")
class BusinessMongoDbEntity(
    id: String = ObjectId().toHexString(),

    createdDate: LocalDateTime = LocalDateTime.now(),

    lastModifiedDate: LocalDateTime = createdDate,

    // 상호명
    val name: String,

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

    // 평균 객단가
    val averageOrderAmount: Double,

    // 좌석 수
    val seatCount: Int,
) : BaseMongoDbEntity(id, createdDate, lastModifiedDate)
