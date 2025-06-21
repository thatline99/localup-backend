package thatline.localup.localup.request

import jakarta.validation.constraints.NotBlank

class SearchTouristAttractionConcentrationLast30DaysRequest(
    @field:NotBlank
    val areaCd: String,

    @field:NotBlank
    val signguCd: String,

    @field:NotBlank
    val tatsNm: String,
)
