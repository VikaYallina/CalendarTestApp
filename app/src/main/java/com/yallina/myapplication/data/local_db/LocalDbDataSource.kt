package com.yallina.myapplication.data.local_db

import com.yallina.myapplication.data.local_db.entity.TaskEntity
import com.yallina.myapplication.domain.model.Task
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime

interface LocalDbDataSource {
    fun selectAll(): Flow<List<Task>>

    fun selectTaskById(id: Int): Flow<Task>

    fun selectTasksBetweenDates(dateStart: LocalDateTime, dateEnd: LocalDateTime): Flow<List<Task>>

    suspend fun insertTask(task: Task)
}