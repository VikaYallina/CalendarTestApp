package com.yallina.myapplication.data

import com.yallina.myapplication.data.local_db.TaskDao
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.domain.repository.TasksRepository
import com.yallina.myapplication.utils.toDataEntity
import com.yallina.myapplication.utils.toDomainModel
import com.yallina.myapplication.utils.toDomainModelList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDateTime

class TasksRepositoryImpl(
    private val tasksDao: TaskDao
) : TasksRepository {
    override fun getAllTasks(): Flow<List<Task>> {
        return tasksDao.selectAll().map { list -> list.toDomainModelList() }
    }

    override fun getTaskById(id: Int): Flow<Task> {
        return tasksDao.selectTaskById(id).map { it.toDomainModel() }
    }

    override fun getTaskInTimeInterval(
        dateStart: LocalDateTime,
        dateEnd: LocalDateTime
    ): Flow<List<Task>> {
        return tasksDao.selectTasksBetweenDates(dateStart, dateEnd)
            .map { list -> list.toDomainModelList() }

    }

    //TODO: посмотерть правильно ли я везде пишу startDate или dateStart

    override suspend fun addTask(task: Task) {
        tasksDao.insertTask(task.toDataEntity())
    }
}