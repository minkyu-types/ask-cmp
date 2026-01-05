package dev.loki.ask.model

import dev.loki.domain.type.Category
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class GoalModel(
    override val id: Int,
    override val title: String,
    override val category: Category,
    override val registeredDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    override val endDate: LocalDate,
    val mileStones: List<MilestoneModel> = emptyList(),
    override val receiveNotification: Boolean,
) : HierarchicalModel<MilestoneModel> {
    override val children: List<MilestoneModel> get() = mileStones
}
