package com.yallina.myapplication.presentation.task_info_screen.mapper

import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.presentation.task_info_screen.model.TaskInfoPresentationModel
import com.yallina.myapplication.utils.MyDateTimeFormatter
import org.threeten.bp.format.DateTimeFormatter

private fun createPresentationTaskInfoFromModel(
    model: Task,
    formatter: DateTimeFormatter = MyDateTimeFormatter.isoLocalDateFormatter
) = TaskInfoPresentationModel(
    name = model.name,
    description = model.description,
    dateStart = model.dateStart.format(MyDateTimeFormatter.formatter),
    dateEnd = model.dateEnd.format(MyDateTimeFormatter.formatter)
)

/**
 * Kotlin extension function that converts [Task] object into [TaskInfoPresentationModel]
 */
fun Task.toInfoPresentation() = createPresentationTaskInfoFromModel(this)