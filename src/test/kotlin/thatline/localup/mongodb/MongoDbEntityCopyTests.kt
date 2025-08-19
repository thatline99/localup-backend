package thatline.localup.mongodb

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.TestPropertySource
import thatline.localup.user.entity.BusinessMongoDbEntity
import thatline.localup.user.entity.CustomerSegment
import thatline.localup.user.repository.BusinessMongoDbRepository

@DataMongoTest
@TestPropertySource(properties = ["spring.data.mongodb.database=test"])
class MongoDbEntityCopyTests {
    @Autowired
    lateinit var businessMongoDbRepository: BusinessMongoDbRepository

    // 얕은 복사 테스트용 엔티티
    private fun BusinessMongoDbEntity.shallowCopySameSet(): BusinessMongoDbEntity =
        BusinessMongoDbEntity(
            id = this.id,
            createdDate = this.createdDate,
            lastModifiedDate = this.lastModifiedDate,
            name = this.name,
            sigunguCode = this.sigunguCode,
            zipCode = this.zipCode,
            address = this.address,
            addressDetail = this.addressDetail,
            latitude = this.latitude,
            longitude = this.longitude,
            type = this.type,
            item = this.item,
            averageOrderAmount = this.averageOrderAmount,
            seatCount = this.seatCount,
            customerSegments = this.customerSegments // Set 인스턴스 참조(얕은 복사)
        )

    @Test
    fun test() {
        // given
        val originalMongoDbEntity = BusinessMongoDbEntity(
            name = "",
            sigunguCode = "",
            zipCode = "",
            address = "",
            addressDetail = null,
            latitude = 0.0,
            longitude = 0.0,
            type = "",
            item = "",
            averageOrderAmount = 0.0,
            seatCount = 10,
            customerSegments = setOf(CustomerSegment.FAMILY)
        )

        val savedMongoDbEntity = businessMongoDbRepository.save(originalMongoDbEntity)

        // when
        val foundMongoDbEntity = businessMongoDbRepository.findById(savedMongoDbEntity.id)
            .orElseThrow()

        val copiedMongoDbEntity = foundMongoDbEntity.update(
            name = "테스트",
            customerSegments = setOf(CustomerSegment.COUPLE),
        )
        val shallowCopiedMongoDbEntity = foundMongoDbEntity.shallowCopySameSet()

        // 원본 Set 수정 시도
        (foundMongoDbEntity.customerSegments as? MutableSet<CustomerSegment>)
            ?.add(CustomerSegment.COUPLE)

        // then
        assertThat(copiedMongoDbEntity.id).isEqualTo(foundMongoDbEntity.id)
        assertThat(copiedMongoDbEntity.name).isEqualTo("테스트")
        assertThat(copiedMongoDbEntity.customerSegments).containsExactly(CustomerSegment.COUPLE)

        // 원본에 COUPLE 추가해도 copy에는 영향 없음
        assertThat(copiedMongoDbEntity.customerSegments).doesNotContain(CustomerSegment.FAMILY)

        // then (얕은 복사 검증)
        // shallow copy는 같은 Set 참조를 쓰므로 원본 수정이 반영됨
        assertThat(shallowCopiedMongoDbEntity.customerSegments).contains(CustomerSegment.COUPLE)
        assertThat(shallowCopiedMongoDbEntity.customerSegments).contains(CustomerSegment.FAMILY)
    }
}
