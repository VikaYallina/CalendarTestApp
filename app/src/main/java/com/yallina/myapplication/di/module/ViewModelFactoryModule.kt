package com.yallina.myapplication.di.module

import android.content.Context
import com.yallina.myapplication.domain.use_case.AddNewTaskUseCase
import com.yallina.myapplication.domain.use_case.GetTaskByIdUseCase
import com.yallina.myapplication.domain.use_case.GetTasksOnDayUseCase
import com.yallina.myapplication.domain.use_case.InitDataFromFileUseCase
import com.yallina.myapplication.presentation.new_task_screeen.NewTaskViewModelFactory
import com.yallina.myapplication.presentation.task_info_screen.TaskInfoViewModelFactory
import com.yallina.myapplication.presentation.task_select_screen.TaskSelectViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelFactoryModule {

    @Provides
    @Singleton
    fun provideTaskSelectViewModelFactory(
        getTasksOnDayUseCase: GetTasksOnDayUseCase,
        initDataFromFileUseCase: InitDataFromFileUseCase,
        context: Context
    ): TaskSelectViewModelFactory =
        TaskSelectViewModelFactory(getTasksOnDayUseCase, initDataFromFileUseCase, context)

    @Provides
    @Singleton
    fun provideTaskInfoViewModelFactory(
        getTaskByIdUseCase: GetTaskByIdUseCase
    ): TaskInfoViewModelFactory = TaskInfoViewModelFactory(getTaskByIdUseCase)

    @Provides
    @Singleton
    fun provideNewTaskViewModelFactory(
        addNewTaskUseCase: AddNewTaskUseCase
    ): NewTaskViewModelFactory = NewTaskViewModelFactory(addNewTaskUseCase)
}