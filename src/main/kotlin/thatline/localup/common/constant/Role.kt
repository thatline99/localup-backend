package thatline.localup.common.constant

enum class Role {
    MASTER,
    USER;

    fun toAuthority(): String = "ROLE_$name"
}
