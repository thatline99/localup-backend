package thatline.localup.common.util

import java.time.format.DateTimeFormatter

object DateTimeUtil {
    val DATETIME_FORMATTER_yyyyMMdd: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val DATETIME_FORMATTER_yyyyMMddHHmm: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
    val DATETIME_FORMATTER_HHmm: DateTimeFormatter = DateTimeFormatter.ofPattern("HHmm")
}
