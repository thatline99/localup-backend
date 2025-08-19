package thatline.localup.common.configuration

object MongoDbCommandCounter {
    private val commandCount = ThreadLocal.withInitial { 0 }

    fun increment() {
        commandCount.set(commandCount.get() + 1)
    }

    fun get(): Int = commandCount.get()

    fun reset() {
        commandCount.set(0)
    }

    fun clear() {
        commandCount.remove()
    }
}
