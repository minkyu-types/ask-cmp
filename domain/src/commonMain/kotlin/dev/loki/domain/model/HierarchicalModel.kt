package dev.loki.domain.model

import dev.loki.domain.type.Category
import kotlinx.datetime.LocalDate

/**
 * 계층 구조를 나타내는 공통 인터페이스
 * @param Child 자식 요소의 타입. 최하위 레벨의 경우 Nothing 사용
 */
sealed interface HierarchicalModel<out Child> {
    val id: Int
    val title: String
    val category: Category
    val registeredDate: LocalDate
    val endDate: LocalDate
    val children: List<Child>
    val receiveNotification: Boolean
}
