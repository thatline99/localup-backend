package thatline.localup.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["thatline.localup.repository.mongodb"])
class MongoDbConfiguration
