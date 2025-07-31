package thatline.localup.common.annotation

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@PreAuthorize("hasAnyRole('MASTER', 'MANAGER', 'USER')")
annotation class RequireUser
