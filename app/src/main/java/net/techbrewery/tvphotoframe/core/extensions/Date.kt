package net.techbrewery.tvphotoframe.core.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.parseToLocalDateTime(pattern: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return LocalDateTime.parse(this, formatter)
}

const val GOOGLE_PHOTOS_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'" //"2022-07-14T16:39:32Z"

fun String.parseGooglePhotosDate(): LocalDateTime = parseToLocalDateTime(GOOGLE_PHOTOS_DATE_PATTERN)
