package dev.loki.domain.model

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate

/**
 * @param period 기간(개월 수 + 일 수)
 * @param hasSucceeded 성공 여부
 */
data class Mission(
    val id: Int,
    val title: String,
    val body: String,
    val period: DatePeriod,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val hasSucceeded: Boolean,
)
