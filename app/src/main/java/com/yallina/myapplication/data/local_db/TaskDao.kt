package com.yallina.myapplication.data.local_db

import android.database.sqlite.SQLiteException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yallina.myapplication.data.local_db.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun selectAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun selectTaskById(id: Int): Flow<TaskEntity>

    @Query(
        "SELECT * FROM tasks WHERE datetime(date_start) <= datetime(:dateEnd) " +
                "AND datetime(date_end) >= datetime(:dateStart)"
    )
    fun selectTasksBetweenDates(dateStart: LocalDateTime, dateEnd: LocalDateTime): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Throws(SQLiteException::class)
    suspend fun insertTask(task: TaskEntity)
}