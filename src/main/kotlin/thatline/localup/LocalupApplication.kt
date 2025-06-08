package thatline.localup

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("thatline.localup.property")
class LocalupApplication

fun main(args: Array<String>) {
    runApplication<LocalupApplication>(*args)
}
