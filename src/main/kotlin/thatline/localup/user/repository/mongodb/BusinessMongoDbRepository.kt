package thatline.localup.user.repository.mongodb

import org.springframework.data.mongodb.repository.MongoRepository
import thatline.localup.user.entity.mongodb.BusinessMongoDbEntity

interface BusinessMongoDbRepository : MongoRepository<BusinessMongoDbEntity, String>
