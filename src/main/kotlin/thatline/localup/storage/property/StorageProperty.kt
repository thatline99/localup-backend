package thatline.localup.storage.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "storage")
data class StorageProperty(
    var basePath: String = "./uploads"
)