package com.yallina.myapplication.data.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yallina.myapplication.data.local_db.converter.LocalDateTimeConverter
import com.yallina.myapplication.data.local_db.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}