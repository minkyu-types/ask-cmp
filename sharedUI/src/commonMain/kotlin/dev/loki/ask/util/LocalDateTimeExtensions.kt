package dev.loki.ask.util

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.plus

fun LocalDateTime.plusDays(days: Int): LocalDateTime {
    val date = this.date.plus(DatePeriod(days = days))
    return LocalDateTime(
        year = date.year,
        month = date.month,
        day = date.day,
        hour = this.hour,
        minute = this.minute,
        second = this.second,
        nanosecond = this.nanosecond
    )
}

fun LocalDateTime.plusMonths(months: Int): LocalDateTime {
    val date = this.date.plus(months, DateTimeUnit.MONTH)
    return LocalDateTime(
        year = date.year,
        month = date.month,
        day = date.day,
        hour = this.hour,
        minute = this.minute,
        second = this.second,
        nanosecond = this.nanosecond
    )
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDateTime.toFormatted(pattern: String = "yyyy.MM.dd"): String {
    val format = LocalDateTime.Format { byUnicodePattern(pattern) }
    return this.format(format)
}

fun LocalDateTime.toDayAndDayOfWeek(): String {
    val datePart = this.toFormatted("d일")
    val dayOfWeek = this.dayOfWeek.toKorean()
    return "$datePart $dayOfWeek"
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDate.toFormatted(pattern: String = "yyyy.MM.dd"): String {
    val format = LocalDate.Format { byUnicodePattern(pattern) }
    return this.format(format)
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun String.toLocalDateTime(pattern: String): LocalDateTime {
    val format = LocalDateTime.Format { byUnicodePattern(pattern) }
    return LocalDateTime.parse(this, format)
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun String.toLocalDate(pattern: String): LocalDate {
    val format = LocalDate.Format { byUnicodePattern(pattern) }
    return LocalDate.parse(this, format)
}

/**
 * "yyyyMMdd" 형식의 날짜 문자열에서 다음 달의 월 값을 Int로 반환
 * 예: "20251231" -> 1 (2026년 1월)
 */
fun String.getNextMonthValue(): Int {
    val date = this.toLocalDate("yyyyMMdd")
    val nextMonthDate = date.plus(1, DateTimeUnit.MONTH)
    return nextMonthDate.monthNumber
}

/**
 * 영문 dayOfWeek -> 한글 dayOfWeek 변환
 */
fun DayOfWeek.toKorean(): String = when (this) {
    DayOfWeek.MONDAY -> "월요일"
    DayOfWeek.TUESDAY -> "화요일"
    DayOfWeek.WEDNESDAY -> "수요일"
    DayOfWeek.THURSDAY -> "목요일"
    DayOfWeek.FRIDAY -> "금요일"
    DayOfWeek.SATURDAY -> "토요일"
    DayOfWeek.SUNDAY -> "일요일"
    else -> ""
}