package thatline.localup

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication(exclude = [SecurityAutoConfiguration::class]) // TODO: noah, 스프링 시큐리티 임시 비활성화, delete
class LocalupApplication

fun main(args: Array<String>) {
    runApplication<LocalupApplication>(*args)
}
