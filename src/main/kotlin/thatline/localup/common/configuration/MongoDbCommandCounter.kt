package thatline.localup.common.configuration

object MongoDbCommandCounter {
    data class EndResult(
        val isOutermostExit: Boolean,
        val totalCommandCount: Int = 0,
    )

    private data class CounterState(var depth: Int = 0, var commandCount: Int = 0)

    private val threadLocalState = ThreadLocal.withInitial { CounterState() }

    fun begin() {
        val state = threadLocalState.get()

        state.depth += 1

        if (state.depth == 1) {
            state.commandCount = 0
        }
    }

    fun end(): EndResult {
        val state = threadLocalState.get()

        state.depth -= 1

        val isOutermostExit = (state.depth == 0)

        return if (isOutermostExit) {
            val totalCommands = state.commandCount

            threadLocalState.remove()

            EndResult(isOutermostExit = true, totalCommandCount = totalCommands)
        } else {
            EndResult(isOutermostExit = false, totalCommandCount = 0)
        }
    }

    fun increment() {
        val state = threadLocalState.get()

        if (state.depth > 0) {
            state.commandCount += 1
        }
    }
}
