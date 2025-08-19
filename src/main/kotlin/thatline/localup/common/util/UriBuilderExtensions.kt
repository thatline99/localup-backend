package thatline.localup.common.util

import org.springframework.web.util.UriComponentsBuilder

fun UriComponentsBuilder.queryParamIfNotNull(name: String, value: Any?) = apply {
    if (value != null) queryParam(name, value)
}
