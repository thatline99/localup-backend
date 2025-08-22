package thatline.localup

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = ["thatline.localup.common.property", "thatline.localup.storage.property"])
class LocalupApplication

fun main(args: Array<String>) {
    runApplication<LocalupApplication>(*args)

    // 클로드 sonnet 모델을 이용한 테스트 PR 용 입니다.
    println("Localup Application is running!")
}
