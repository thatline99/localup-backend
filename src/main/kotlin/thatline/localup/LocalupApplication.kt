package thatline.localup

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = ["thatline.localup.common.property", "thatline.localup.storage.property"])
class LocalupApplication

fun main(args: Array<String>) {
    runApplication<LocalupApplication>(*args)
}
