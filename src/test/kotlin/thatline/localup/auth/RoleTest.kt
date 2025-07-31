package thatline.localup.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class RoleTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `ROLE_MASTER가 master 접근`() {
        mockMvc.perform(get("/api/admin/master").with(user("username").roles("MASTER")))
            .andExpect(status().isOk)
    }

    @Test
    fun `ROLE_USER가 master 접근`() {
        mockMvc.perform(get("/api/admin/master").with(user("username").roles("USER")))
            .andExpect(status().isForbidden)
    }

    @Test
    fun `ROLE_MASTER가 user 접근`() {
        mockMvc.perform(get("/api/admin/user").with(user("username").roles("MASTER")))
            .andExpect(status().isOk)
    }

    @Test
    fun `ROLE_USER가 user 접근`() {
        mockMvc.perform(get("/api/admin/user").with(user("username").roles("USER")))
            .andExpect(status().isOk)
    }
}
