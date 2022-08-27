package com.yallina.myapplication.domain.repository

import com.yallina.myapplication.domain.model.Task
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime


interface TasksRepository {
    fun getAllTasks(): Flow<List<Task>>

    fun getTaskById(id: Int): Flow<Task>

    fun getTaskInTimeInterval(dateStart: LocalDateTime, dateEnd: LocalDateTime): Flow<List<Task>>

    suspend fun addTask(task: Task)
}
