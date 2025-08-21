package thatline.localup.common.annotation

import thatline.localup.common.util.DateTimeUtil
import java.time.DayOfWeek
import java.time.LocalDate

enum class ApiWindow(
    private val generateKey: () -> String,
    val ttl: Long,
) {
    DAILY({
        LocalDate.now().atStartOfDay().format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMddHHmmss)
    }, 86400),

    WEEKLY({
        LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay().format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMddHHmmss)
    }, 604800),

    MONTHLY({
        LocalDate.now().withDayOfMonth(1).atStartOfDay().format(DateTimeUtil.DATETIME_FORMATTER_yyyyMMddHHmmss)
    }, 2592000);

    fun getKey(): String = generateKey()
}
