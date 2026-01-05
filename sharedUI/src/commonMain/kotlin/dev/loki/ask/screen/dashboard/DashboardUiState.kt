package dev.loki.ask.screen.dashboard

import dev.loki.ask.model.GoalModel

data class DashboardUiState(
    val goals: List<GoalModel> = emptyList()
)
