package com.yallina.myapplication.presentation.task_select_screen.model

import com.yallina.myapplication.domain.model.Task
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Presentation model for [Task] domain entity. It is used to display all tasks that are active
 * for a specified hour of the day
 * @param timeStart [LocalTime] for a start of the specified hour
 * @param timeEnd [LocalTime] for an end of the specified hour
 * @param taskList a List of [Task] that occur during the specified hour
 */
data class TaskPresentationModel(
    val timeStart: LocalTime?,
    val timeEnd: LocalTime?,
    var taskList: List<Task> = emptyList()
) {
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

    /**
     * Creates a String representation of the time between [timeStart] and [timeEnd] in format:
     * hh:mm-hh:mm
     * @return String object
     */
    fun durationAsString(): String {
        val timeStartStr = timeStart?.format(formatter)?.dropLast(3)
        val timeEndStr = timeEnd?.format(formatter)?.dropLast(3)

        return "$timeStartStr - $timeEndStr"
    }
}