package thatline.localup.common.configuration

import com.mongodb.event.CommandListener
import com.mongodb.event.CommandStartedEvent

class MongoDbCountingCommandListener : CommandListener {
    override fun commandStarted(event: CommandStartedEvent) {
        MongoDbCommandCounter.increment()
    }
}
