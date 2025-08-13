package thatline.localup.common.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(
    basePackages = [
        "thatline.localup.auth.repository.mongodb",
        "thatline.localup.user.repository.mongodb",
        "thatline.localup.storage.repository.mongodb",
    ]
)
class MongoDbConfiguration
