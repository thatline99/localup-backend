package thatline.localup.user.service

import org.springframework.stereotype.Service
import thatline.localup.user.entity.mongodb.UserMongoDbEntity
import thatline.localup.user.exception.UserNotFoundException
import thatline.localup.user.repository.mongodb.UserMongoDbRepository
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserMongoDbRepository,
) {
    fun updateAddress(
        id: String,
        zipCode: String,
        address: String,
        addressDetail: String?,
        latitude: Double,
        longitude: Double,
    ) {
        val user = userRepository.findById(id)
            .orElseThrow { UserNotFoundException() }

        val updatedUser = UserMongoDbEntity(
            id = user.id,
            createdDate = user.createdDate,
            lastModifiedDate = LocalDateTime.now(),

            email = user.email,
            password = user.password,

            zipCode = zipCode,
            address = address,
            addressDetail = addressDetail,
            latitude = latitude,
            longitude = longitude
        )

        userRepository.save(updatedUser)
    }
}
