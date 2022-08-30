package com.yallina.myapplication.data.local_db.mapper

import com.yallina.myapplication.data.local_db.entity.TaskEntity
import com.yallina.myapplication.domain.model.Task
import org.threeten.bp.LocalDateTime

private fun createTaskEntityFromModel(task: Task) = TaskEntity(
    id = task.id,
    name = task.name,
    description = task.description,
    dateStart = task.dateStart,
    dateEnd = task.dateEnd
)

/**
 * Kotlin extension function that converts [Task] to [TaskEntity]
 */
fun Task.toDataEntity() = createTaskEntityFromModel(this)


/**
 * Kotlin extension function that converts List of [Task] to List of [TaskEntity]
 */
fun List<Task>.toDataEntityList(): List<TaskEntity> =
    this.map { task -> createTaskEntityFromModel(task) }

private fun createTaskModelFromEntity(taskEntity: TaskEntity) =
    Task(
        id = taskEntity.id,
        name = taskEntity.name,
        description = taskEntity.description ?: "",
        dateStart = taskEntity.dateStart ?: LocalDateTime.now(),
        dateEnd = taskEntity.dateEnd ?: LocalDateTime.now().plusHours(1)
    )


/**
 * Kotlin extension function that converts [TaskEntity] to [Task]
 */
fun TaskEntity.toDomainModel(): Task = createTaskModelFromEntity(this)


/**
 * Kotlin extension function that converts a List of [TaskEntity] to a List of [Task]
 */
fun List<TaskEntity>.toDomainModelList() = this.map { task -> createTaskModelFromEntity(task) }


