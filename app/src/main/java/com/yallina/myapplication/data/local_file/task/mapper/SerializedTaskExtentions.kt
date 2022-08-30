package com.yallina.myapplication.data.local_file.task.mapper

import com.yallina.myapplication.data.local_file.task.model.SerializedTask
import com.yallina.myapplication.domain.model.Task
import org.threeten.bp.LocalDateTime

private fun createTaskModelFromSerialized(task: SerializedTask) = Task(
    id = 0,
    name = task.name ?: "",
    description = task.description ?: "",
    dateStart = task.dateStart ?: LocalDateTime.now(),
    dateEnd = task.dateEnd ?: LocalDateTime.now().plusDays(1)
)

/**
 * Kotlin extension function that converts [SerializedTask] to [Task]
 */
fun SerializedTask.toDomainModel() = createTaskModelFromSerialized(this)

/**
 * Kotlin extension function that converts a list of [SerializedTask] to a list of [Task]
 */
fun List<SerializedTask>.toDomainModelList() = this.map { createTaskModelFromSerialized(it) }

