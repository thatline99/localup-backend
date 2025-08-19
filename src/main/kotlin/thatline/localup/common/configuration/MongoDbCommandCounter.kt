package thatline.localup.common.configuration

object MongoDbCommandCounter {
    private val commandStackTL = ThreadLocal.withInitial { ArrayDeque<Int>() }

    fun startScope() {
        commandStackTL.get().addLast(0)
    }

    fun increment() {
        val stack = commandStackTL.get()

        if (stack.isNotEmpty()) {
            val current = stack.removeLast()

            stack.addLast(current + 1)
        }
    }

    fun finishScope(): CommandCountScopeResult {
        val stack = commandStackTL.get()

        if (stack.isEmpty()) {
            return CommandCountScopeResult(isRootScopeExit = true, commandCountInScope = 0, remainingNestedDepth = 0)
        }

        val scopeCount = stack.removeLast()
        val isRootScopeExit = stack.isEmpty()

        if (!isRootScopeExit) {
            val parent = stack.removeLast()

            stack.addLast(parent + scopeCount)
        } else {
            commandStackTL.remove()
        }

        return CommandCountScopeResult(
            isRootScopeExit = isRootScopeExit,
            commandCountInScope = scopeCount,
            remainingNestedDepth = stack.size
        )
    }

    data class CommandCountScopeResult(
        val isRootScopeExit: Boolean,
        val commandCountInScope: Int = 0,
        val remainingNestedDepth: Int = 0,
    )
}
