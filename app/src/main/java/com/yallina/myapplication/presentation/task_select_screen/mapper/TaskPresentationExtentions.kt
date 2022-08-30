package com.yallina.myapplication.presentation.task_select_screen.mapper

import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.presentation.new_task_screeen.model.NewTaskPresentationModel
import org.threeten.bp.LocalDateTime


private fun createDomainTaskFromNewTaskPresentation(task: NewTaskPresentationModel) = Task(
    id = 0,
    name = task.name ?: "new task",
    description = task.description ?: "new description",
    dateStart = task.dateStart ?: LocalDateTime.now(),
    dateEnd = task.dateEnd ?: LocalDateTime.now().plusHours(1)
)

/**
 * Kotlin extension function that converts [NewTaskPresentationModel] to [Task]
 */
fun NewTaskPresentationModel.toDomainModel() = createDomainTaskFromNewTaskPresentation(this)