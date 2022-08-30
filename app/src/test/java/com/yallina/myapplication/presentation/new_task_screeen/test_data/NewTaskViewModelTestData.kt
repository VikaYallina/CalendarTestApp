package com.yallina.myapplication.presentation.new_task_screeen.test_data

import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.presentation.new_task_screeen.model.NewTaskPresentationModel
import org.threeten.bp.LocalDateTime

object NewTaskViewModelTestData {
    val testingDateTime: LocalDateTime = LocalDateTime.of(2000, 12, 12, 0, 0, 0)

    val taskBlankDescription = Task(
        0,
        "Name",
        "    ",
        testingDateTime,
        testingDateTime.plusDays(1)
    )

    val taskEndDateBeforeStart = Task(
        0,
        "Name",
        "desc",
        testingDateTime,
        testingDateTime.minusSeconds(1)
    )

    val taskSaveSuccessResult = Task(
        0,
        "Success",
        "desc",
        testingDateTime,
        testingDateTime.plusDays(1)
    )

    val taskSaveErrorResult = Task(
        0,
        "Error",
        "desc",
        testingDateTime,
        testingDateTime.plusDays(1)
    )
}