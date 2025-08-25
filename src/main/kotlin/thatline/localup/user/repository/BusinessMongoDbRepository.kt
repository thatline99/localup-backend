package thatline.localup.user.repository

import org.springframework.data.mongodb.repository.MongoRepository
import thatline.localup.user.entity.BusinessMongoDbEntity

interface BusinessMongoDbRepository : MongoRepository<BusinessMongoDbEntity, String>
