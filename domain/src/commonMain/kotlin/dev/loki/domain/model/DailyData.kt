package dev.loki.domain.model

import kotlinx.datetime.LocalDate

data class DailyData(
    val id: Int,
    val date: LocalDate,
    val quote: Quote,
    val motivations: List<HierarchicalModel<*>>,
    val currentProgress: Float,
    val totalProgress: Int,
)