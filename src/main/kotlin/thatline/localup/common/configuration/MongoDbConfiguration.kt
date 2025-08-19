package thatline.localup.common.configuration

import com.mongodb.event.CommandFailedEvent
import com.mongodb.event.CommandListener
import com.mongodb.event.CommandStartedEvent
import com.mongodb.event.CommandSucceededEvent
import org.bson.json.JsonWriterSettings
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongoDbConfiguration {

    @Bean
    fun mongoClientSettingsBuilderCustomizer() = MongoClientSettingsBuilderCustomizer { builder ->
        builder.addCommandListener(object : CommandListener {
            private val log = LoggerFactory.getLogger("mongodb-query")
            private val jsonWriterSettings = JsonWriterSettings.builder().indent(true).build()

            override fun commandStarted(event: CommandStartedEvent) {
                if (log.isDebugEnabled) {
                    log.debug("\nMongoDB Query:\n${event.command.toJson(jsonWriterSettings)}")
                }
            }

            override fun commandSucceeded(event: CommandSucceededEvent) {}
            override fun commandFailed(event: CommandFailedEvent) {}
        })
    }
}
