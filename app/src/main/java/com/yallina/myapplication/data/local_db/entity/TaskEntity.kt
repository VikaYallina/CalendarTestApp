package com.yallina.myapplication.data.local_db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "desc") val description: String?,
    @ColumnInfo(name = "date_start") val dateStart: LocalDateTime?,
    @ColumnInfo(name = "date_end") val dateEnd: LocalDateTime?
)