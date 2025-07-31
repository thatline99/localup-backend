package thatline.localup.admin.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import thatline.localup.common.annotation.RequireMaster
import thatline.localup.common.annotation.RequireUser

@RestController
@RequestMapping("/api/admin")
class AdminController {

    // TODO: 테스트 코드 삭제
    @GetMapping("/master")
    @RequireMaster
    fun master(): ResponseEntity<Unit> {
        return ResponseEntity.ok().build()
    }

    // TODO: 테스트 코드 삭제
    @GetMapping("/user")
    @RequireUser
    fun user(): ResponseEntity<Unit> {
        return ResponseEntity.ok().build()
    }
}
