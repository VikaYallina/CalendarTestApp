package com.yallina.myapplication.presentation.task_select_screen.model

import com.yallina.myapplication.domain.model.Task
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

data class TaskPresentationModel(
    val timeStart: LocalTime?,
    val timeEnd: LocalTime?,
    var taskList: List<Task> = emptyList()
) {
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

    fun durationAsString(): String {
        val timeStartStr = timeStart?.format(formatter)?.dropLast(3)
        val timeEndStr = timeEnd?.format(formatter)?.dropLast(3)

        return "$timeStartStr - $timeEndStr"
    }
}