package com.yallina.myapplication.domain.use_case

import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

class GetTasksOnDay(
    private val tasksRepository: TasksRepository
) {
    fun execute(day: LocalDate): Flow<List<Task>> {
        val startDateTime = LocalDateTime.of(day, LocalTime.of(0,0,0))
        val endDateTime = LocalDateTime.of(day, LocalTime.of(23,59,59))
        return tasksRepository.getTaskInTimeInterval(startDateTime, endDateTime)
    }
}