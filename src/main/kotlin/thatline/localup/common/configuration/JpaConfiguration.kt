package thatline.localup.common.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["thatline.localup.repository.jpa"])
class JpaConfiguration
