package thatline.localup.request

import jakarta.validation.constraints.NotBlank

data class SearchTouristAttractionRequest(
    @field:NotBlank
    val areaCd: String,

    @field:NotBlank
    val signguCd: String,
)
