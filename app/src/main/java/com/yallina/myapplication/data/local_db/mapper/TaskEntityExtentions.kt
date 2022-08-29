package com.yallina.myapplication.utils

import com.yallina.myapplication.data.local_db.entity.TaskEntity
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.presentation.new_task_screeen.model.NewTaskPresentationModel
import com.yallina.myapplication.presentation.task_info_screen.model.TaskInfoPresentationModel
import com.yallina.myapplication.utils.MyDateTimeFormatter.formatter
import org.threeten.bp.LocalDateTime

private fun createTaskEntityFromModel(task: Task) = TaskEntity(
    id = task.id,
    name = task.name,
    description = task.description,
    dateStart = task.dateStart,
    dateEnd = task.dateEnd
)

fun Task.toDataEntity() = createTaskEntityFromModel(this)

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

fun TaskEntity.toDomainModel(): Task = createTaskModelFromEntity(this)

fun List<TaskEntity>.toDomainModelList() = this.map { task -> createTaskModelFromEntity(task) }


