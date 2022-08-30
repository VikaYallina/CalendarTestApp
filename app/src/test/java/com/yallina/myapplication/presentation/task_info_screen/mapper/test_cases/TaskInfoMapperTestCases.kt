package com.yallina.myapplication.presentation.task_info_screen.mapper.test_cases

import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.presentation.task_info_screen.model.TaskInfoPresentationModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

object TaskInfoMapperTestCases {
    val isoLocalDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val customFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss")

    val testTask = Task(
        0,
        "name",
        "description",
        LocalDateTime.of(2000, 12, 31, 16, 34, 55),
        LocalDateTime.of(2001, 1, 15, 17, 0, 9)
    )

    val defaultPresentationTask = TaskInfoPresentationModel(
        "name",
        "description",
        "декабря 31, 2000 16:34:55",
        "января 15, 2001 17:00:09"
    )

    val isoPresentationTask = TaskInfoPresentationModel(
        "name",
        "description",
        "2000-12-31T16:34:55",
        "2001-01-15T17:00:09"
    )

    val customPresentationTask = TaskInfoPresentationModel(
        "name",
        "description",
        "31.12.2000 - 16:34:55",
        "15.01.2001 - 17:00:09"
    )

}