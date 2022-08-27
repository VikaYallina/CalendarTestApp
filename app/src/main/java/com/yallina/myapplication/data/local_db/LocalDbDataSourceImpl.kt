package com.yallina.myapplication.data.local_db

import com.yallina.myapplication.data.local_db.entity.TaskEntity
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.utils.EntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDateTime

class LocalDbDataSourceImpl(
    private val taskDao: TaskDao,
    private val taskModelToEntityMapper: EntityMapper<Task, TaskEntity>,
    private val taskEntityToModelMapper: EntityMapper<TaskEntity, Task>
) :LocalDbDataSource{
    override fun selectAll(): Flow<List<Task>> {
        return taskDao.selectAll().map { list -> taskEntityToModelMapper.transform(list) }
    }

    override fun selectTaskById(id: Int): Flow<Task> {
        return taskDao.selectTaskById(id).map { taskEntityToModelMapper.transform(it) }
    }

    override fun selectTasksBetweenDates(
        dateStart: LocalDateTime,
        dateEnd: LocalDateTime
    ): Flow<List<Task>> {
        return taskDao.selectTasksBetweenDates(dateStart, dateEnd).map { list -> taskEntityToModelMapper.transform(list) }
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(taskModelToEntityMapper.transform(task))
    }
}