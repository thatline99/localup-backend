package thatline.localup.user.dto

data class UserProfileDto(
    val email: String,
    val name: String?,
    val phoneNumber: String?,
    val position: String?,
    val profileImage: String?,
    val isProfileCompleted: Boolean,
    val hasBusinessInfo: Boolean,
)