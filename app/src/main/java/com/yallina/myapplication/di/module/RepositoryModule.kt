package com.yallina.myapplication.di.module

import android.content.Context
import com.yallina.myapplication.data.TasksRepositoryImpl
import com.yallina.myapplication.data.local_db.TaskDao
import com.yallina.myapplication.domain.repository.TasksRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideTasksRepository(
        taskDao: TaskDao
    ): TasksRepository = TasksRepositoryImpl(taskDao)
}