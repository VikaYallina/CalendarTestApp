package com.yallina.myapplication.di.module

import com.yallina.myapplication.domain.repository.TasksRepository
import com.yallina.myapplication.domain.use_case.AddNewTaskUseCase
import com.yallina.myapplication.domain.use_case.GetAllTasksUseCase
import com.yallina.myapplication.domain.use_case.GetTaskByIdUseCase
import com.yallina.myapplication.domain.use_case.GetTasksOnDayUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {
    @Provides
    @Singleton
    fun provideAddNewTaskUseCase(
        tasksRepository: TasksRepository
    ): AddNewTaskUseCase = AddNewTaskUseCase(tasksRepository)

    @Provides
    @Singleton
    fun provideGetAllTasksUseCase(
        tasksRepository: TasksRepository
    ): GetAllTasksUseCase = GetAllTasksUseCase(tasksRepository)

    @Provides
    @Singleton
    fun provideGetTaskByIdUseCase(
        tasksRepository: TasksRepository
    ): GetTaskByIdUseCase = GetTaskByIdUseCase(tasksRepository)

    @Provides
    @Singleton
    fun provideGetTasksOnDay(
        tasksRepository: TasksRepository
    ): GetTasksOnDayUseCase = GetTasksOnDayUseCase(tasksRepository)
}