package thatline.localup.common.configuration

import com.mongodb.event.CommandListener
import com.mongodb.event.CommandStartedEvent
import org.bson.json.JsonWriterSettings
import org.slf4j.LoggerFactory

class MongoDbLoggingCommandListener : CommandListener {
    companion object {
        private val logger = LoggerFactory.getLogger("mongodb-command")
        private val jsonWriterSettings = JsonWriterSettings.builder().indent(true).build()
    }

    override fun commandStarted(event: CommandStartedEvent) {
        if (logger.isDebugEnabled) {
            logger.debug("\nMongoDB Command:\n${event.command.toJson(jsonWriterSettings)}")
        }
    }
}
