package thatline.localup.common.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import thatline.localup.common.annotation.CountMongoDbCommands
import thatline.localup.common.configuration.MongoDbCommandCounter

@Aspect
@Component
class MongoDbCommandCountAspect {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    @Around("@annotation(countMongoDbCommands)")
    fun countCommands(
        joinPoint: ProceedingJoinPoint,
        countMongoDbCommands: CountMongoDbCommands,
    ): Any? {
        MongoDbCommandCounter.begin()
        return try {
            joinPoint.proceed()
        } finally {
            val endResult = MongoDbCommandCounter.end()
            if (endResult.isOutermostExit && logger.isDebugEnabled) {
                logger.debug(
                    "{} executed {} MongoDB commands",
                    joinPoint.signature.toShortString(),
                    endResult.totalCommandCount
                )
            }
        }
    }
}
