package thatline.localup.common.configuration

import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongoDbConfiguration {

    @Bean
    fun mongoClientSettingsBuilderCustomizer() = MongoClientSettingsBuilderCustomizer { builder ->
        builder
            .addCommandListener(MongoDbCountingCommandListener())
            .addCommandListener(MongoDbLoggingCommandListener())
    }
}
