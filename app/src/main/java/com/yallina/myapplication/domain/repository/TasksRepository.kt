package com.yallina.myapplication.domain.repository

import com.yallina.myapplication.domain.model.Task
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime
import com.yallina.myapplication.domain.model.Result
import java.io.InputStream

/**
 * Domain level repository interface for [Task] Model
 */
interface TasksRepository {

    /**
     * Initialize tasks in persistence storage
     * @return [Result] of the operation
     */
    suspend fun initTasks(inputStream: InputStream): Result<Nothing>

    /**
     * Retrieve all existing tasks
     * @return a [Flow] of a List of [Task]
     */
    fun getAllTasks(): Flow<List<Task>>

    /**
     * Retrieve a single [Task] by specified id
     * @param id an [Int] object that represents id of a [Task]
     * @return [Flow] of [Task]
     */
    fun getTaskById(id: Int): Flow<Task>

    /**
     * Retrieve all [Task] that occur in specified period of time
     * @param dateStart represents the starting date and time of a [Task]
     * @param dateEnd represents the closing date and time of a [Task]
     * @return [Flow] of [Task] that fit the time interval
     */
    fun getTaskInTimeInterval(dateStart: LocalDateTime, dateEnd: LocalDateTime): Flow<List<Task>>

    /**
     * Save a new [Task] object into database
     * @param task a [Task] object
     * @return [Result] of the operation
     */
    suspend fun addTask(task: Task): Result<Task>
}
