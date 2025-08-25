package thatline.localup.common.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class OpenApiQuota(
    val name: String,
    val limit: Long,
    val apiWindow: ApiWindow,
)
