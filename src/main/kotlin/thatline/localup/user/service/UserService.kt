package thatline.localup.user.service

import org.springframework.stereotype.Service
import thatline.localup.common.annotation.CountMongoDbCommands
import thatline.localup.user.dto.FindBusinessDto
import thatline.localup.user.entity.BusinessMongoDbEntity
import thatline.localup.user.entity.CustomerSegment
import thatline.localup.user.exception.BusinessAlreadyRegisteredException
import thatline.localup.user.exception.BusinessNotRegisteredException
import thatline.localup.user.exception.UserNotFoundException
import thatline.localup.user.repository.BusinessMongoDbRepository
import thatline.localup.user.repository.UserMongoDbRepository

@Service
class UserService(
    private val userRepository: UserMongoDbRepository,
    private val businessRepository: BusinessMongoDbRepository,
) {
    @CountMongoDbCommands
    fun findBusiness(
        userId: String,
    ): FindBusinessDto {
        val foundUser = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException() }

        val businessId = foundUser.businessId ?: throw BusinessNotRegisteredException()

        val foundBusiness = businessRepository.findById(businessId)
            .orElseThrow { BusinessNotRegisteredException() }

        return FindBusinessDto(
            name = foundBusiness.name,
            sigunguCode = foundBusiness.sigunguCode,
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

    @CountMongoDbCommands
    fun registerBusiness(
        userId: String,
        businessName: String,
        businessSigunguCode: String,
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
        businessDescription: String?,
    ) {
        val foundUser = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException() }

        if (foundUser.businessId != null) {
            throw BusinessAlreadyRegisteredException()
        }

        val newBusiness = BusinessMongoDbEntity(
            name = businessName,
            sigunguCode = businessSigunguCode,
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
            description = businessDescription
        )

        val savedBusiness = businessRepository.save(newBusiness)

        val updatedUser = foundUser.update(
            businessId = savedBusiness.id,
        )

        userRepository.save(updatedUser)
    }

    @CountMongoDbCommands
    fun updateBusiness(
        userId: String,
        businessName: String,
        businessSigunguCode: String,
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
        businessDescription: String?,
    ) {
        val foundUser = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException() }

        val businessId = foundUser.businessId ?: throw BusinessNotRegisteredException()

        val foundBusiness = businessRepository.findById(businessId)
            .orElseThrow { BusinessNotRegisteredException() }

        val updatedBusiness = foundBusiness.update(
            name = businessName,
            sigunguCode = businessSigunguCode,
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
            description = businessDescription,
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
