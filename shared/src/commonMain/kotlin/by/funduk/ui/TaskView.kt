package by.funduk.ui

import by.funduk.model.*
import kotlinx.serialization.Serializable


@Serializable
data class TaskView(
    val id: Int,
    val name: String,
    val rank: Rank?, // task may have no rank
    val tags: List<Tag>,
    val solvedCount: Int = 0,
    val userStatus: TestInfo? = null
) {
    constructor(task: Task) : this(
        task.id ?: throw IllegalArgumentException("Task ID cannot be null"),
        task.name,
        task.rank,
        task.tags,
        task.solvedCount
    )
}
