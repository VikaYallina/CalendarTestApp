package com.yallina.myapplication.presentation.task_info_screen.mapper

import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.presentation.task_info_screen.model.TaskInfoPresentationModel
import com.yallina.myapplication.utils.MyDateTimeFormatter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

private fun createPresentationTaskInfoFromModel(
    model: Task,
    formatter: DateTimeFormatter
) = TaskInfoPresentationModel(
    name = model.name,
    description = model.description,
    dateStart = model.dateStart.format(formatter),
    dateEnd = model.dateEnd.format(formatter)
)

/**
 * Kotlin extension function that converts [Task] object into [TaskInfoPresentationModel].
 * @param formatter needed to format the [LocalDateTime] into a String presentation
 */
fun Task.toInfoPresentation(formatter: DateTimeFormatter = MyDateTimeFormatter.formatter) =
    createPresentationTaskInfoFromModel(this, formatter)