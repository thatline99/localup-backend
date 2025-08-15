package thatline.localup.user.service

import org.springframework.stereotype.Service
import thatline.localup.user.dto.FindBusinessDto
import thatline.localup.user.entity.mongodb.BusinessMongoDbEntity
import thatline.localup.user.entity.mongodb.CustomerSegment
import thatline.localup.user.entity.mongodb.UserMongoDbEntity
import thatline.localup.user.exception.BusinessAlreadyRegisteredException
import thatline.localup.user.exception.BusinessNotRegisteredException
import thatline.localup.user.exception.UserNotFoundException
import thatline.localup.user.repository.mongodb.BusinessMongoDbRepository
import thatline.localup.user.repository.mongodb.UserMongoDbRepository
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserMongoDbRepository,
    private val businessRepository: BusinessMongoDbRepository,
) {
    fun findBusiness(
        userId: String,
    ): FindBusinessDto? {
        val foundUser = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException() }

        val businessId = foundUser.businessId ?: return null

        val foundBusiness = businessRepository.findById(businessId)
            .orElseThrow { BusinessNotRegisteredException() }

        return FindBusinessDto(
            name = foundBusiness.name,
            zipCode = foundBusiness.zipCode,
            address = foundBusiness.address,
            addressDetail = foundBusiness.addressDetail,
            latitude = foundBusiness.latitude,
            longitude = foundBusiness.longitude,
            type = foundBusiness.type,
            item = foundBusiness.item,
            averageOrderAmount = foundBusiness.averageOrderAmount,
            seatCount = foundBusiness.seatCount,
            customerSegments = foundBusiness.customerSegments,
        )
    }

    fun registerBusiness(
        userId: String,
        businessName: String,
        businessZipCode: String,
        businessAddress: String,
        businessAddressDetail: String?,
        businessLatitude: Double,
        businessLongitude: Double,
        businessType: String,
        businessItem: String,
        businessAverageOrderAmount: Double,
        businessSeatCount: Int,
        businessCustomerSegments: Set<CustomerSegment>,
    ) {
        val foundUser = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException() }

        if (foundUser.businessId != null) {
            throw BusinessAlreadyRegisteredException()
        }

        val newBusiness = BusinessMongoDbEntity(
            name = businessName,
            zipCode = businessZipCode,
            address = businessAddress,
            addressDetail = businessAddressDetail,
            latitude = businessLatitude,
            longitude = businessLongitude,
            type = businessType,
            item = businessItem,
            averageOrderAmount = businessAverageOrderAmount,
            seatCount = businessSeatCount,
            customerSegments = businessCustomerSegments,
        )

        val savedBusiness = businessRepository.save(newBusiness)

        val updatedUser = UserMongoDbEntity(
            id = foundUser.id,
            createdDate = foundUser.createdDate,
            lastModifiedDate = LocalDateTime.now(),
            email = foundUser.email,
            password = foundUser.password,
            role = foundUser.role,
            businessId = savedBusiness.id,
        )

        userRepository.save(updatedUser)
    }

    fun updateBusiness(
        userId: String,
        businessName: String,
        businessZipCode: String,
        businessAddress: String,
        businessAddressDetail: String?,
        businessLatitude: Double,
        businessLongitude: Double,
        businessType: String,
        businessItem: String,
        businessAverageOrderAmount: Double,
        businessSeatCount: Int,
        businessCustomerSegments: Set<CustomerSegment>,
    ) {
        val foundUser = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException() }

        val businessId = foundUser.businessId ?: throw BusinessNotRegisteredException()

        val foundBusiness = businessRepository.findById(businessId)
            .orElseThrow { BusinessNotRegisteredException() }

        val updatedBusiness = BusinessMongoDbEntity(
            id = businessId,
            createdDate = foundBusiness.createdDate,
            lastModifiedDate = LocalDateTime.now(),
            name = businessName,
            zipCode = businessZipCode,
            address = businessAddress,
            addressDetail = businessAddressDetail,
            latitude = businessLatitude,
            longitude = businessLongitude,
            type = businessType,
            item = businessItem,
            averageOrderAmount = businessAverageOrderAmount,
            seatCount = businessSeatCount,
            customerSegments = businessCustomerSegments,
        )

        businessRepository.save(updatedBusiness)
    }

    // TODO: 추후
//    fun updateUserRole(id: String, role: Role) {
//        val user = userRepository.findById(id)
//            .orElseThrow { UserNotFoundException() }
//
//        val updatedUser = UserMongoDbEntity(
//            id = user.id,
//            createdDate = user.createdDate,
//            lastModifiedDate = LocalDateTime.now(),
//
//            email = user.email,
//            password = user.password,
//            role = role,
//
//            zipCode = user.zipCode,
//            address = user.address,
//            addressDetail = user.addressDetail,
//            latitude = user.latitude,
//            longitude = user.longitude
//        )
//
//        userRepository.save(updatedUser)
//    }
}
