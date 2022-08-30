package com.yallina.myapplication.data

import com.yallina.myapplication.data.local_db.TaskDao
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.domain.repository.TasksRepository
import com.yallina.myapplication.data.local_db.mapper.toDataEntity
import com.yallina.myapplication.data.local_db.mapper.toDataEntityList
import com.yallina.myapplication.data.local_db.mapper.toDomainModel
import com.yallina.myapplication.data.local_db.mapper.toDomainModelList
import com.yallina.myapplication.data.local_file.base.AssetReader
import com.yallina.myapplication.data.local_file.task.mapper.toDomainModelList
import com.yallina.myapplication.data.local_file.task.model.SerializedTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import com.yallina.myapplication.domain.model.Result
import java.io.InputStream

/**
 * Implementation of domain interface [TasksRepository]
 * @param tasksDao database DAO interface as [TaskDao]
 * @param assetReader an [AssetReader] interface of type [SerializedTask]
 * @param defaultDispatcher [CoroutineDispatcher] with default value of [Dispatchers.IO]
 */
class TasksRepositoryImpl(
    private val tasksDao: TaskDao,
    private val assetReader: AssetReader<SerializedTask>,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TasksRepository {

    override suspend fun initTasks(inputStream: InputStream): Result<Nothing> {
        return try {
            val rowCount = tasksDao.getRowsCount()

            // if tasks table is empty
            if (rowCount == 0) {
                val tasks = withContext(defaultDispatcher) {
                    assetReader.readList(inputStream).toDomainModelList().toDataEntityList()
                }

                // Insert tasks from Assets file
                val insertedRowsIdArray = tasksDao.insertTaskList(tasks)

                if (insertedRowsIdArray.isNotEmpty())
                    Result.Success()
                else Result.Error(Exception("Rows were not inserted"))
            } else
                Result.Success()
        } catch (e: Exception) {
            Result.Error(Exception(e.message))
        }
    }

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

    override suspend fun addTask(task: Task): Result<Task> {
        return try {
            tasksDao.insertTask(task.toDataEntity())
            Result.Success<Nothing>()
        } catch (e: Throwable) {
            Result.Error(Exception(e.message))
        }

    }
}