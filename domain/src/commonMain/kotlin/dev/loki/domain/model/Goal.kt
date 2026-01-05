package dev.loki.domain.model

import dev.loki.domain.type.Category
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class Goal(
    override val id: Int,
    override val title: String,
    override val category: Category,
    override val registeredDate: LocalDate,
    override val endDate: LocalDate,
    val mileStones: List<Milestone> = emptyList(),
    override val receiveNotification: Boolean,
): HierarchicalModel<Milestone> {
    override val children: List<Milestone> = mileStones
}
