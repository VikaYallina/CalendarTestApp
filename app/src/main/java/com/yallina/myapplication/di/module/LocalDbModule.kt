package com.yallina.myapplication.di.module

import android.content.Context
import androidx.room.Room
import com.yallina.myapplication.data.local_db.AppDatabase
import com.yallina.myapplication.data.local_db.TaskDao
import com.yallina.myapplication.utils.Consts
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalDbModule {
    @Provides
    @Singleton
    fun provideLocalDatabaseInstance(appContext: Context): AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        Consts.ROOM_DB_NAME
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideTaskDao(
        db: AppDatabase
    ): TaskDao = db.taskDao()
}