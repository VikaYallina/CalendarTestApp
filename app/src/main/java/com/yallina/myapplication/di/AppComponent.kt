package com.yallina.myapplication.di

import android.content.Context
import com.yallina.myapplication.di.module.*
import com.yallina.myapplication.presentation.new_task_screeen.NewTaskFragment
import com.yallina.myapplication.presentation.task_info_screen.TaskInfoFragment
import com.yallina.myapplication.presentation.task_select_screen.TaskSelectFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    ApplicationModule::class,
    LocalDbModule::class,
    RepositoryModule::class,
    UseCaseModule::class,
    AssetReaderModule::class,
    ViewModelFactoryModule::class
])
@Singleton
interface AppComponent {
    fun inject(taskSelectFragment: TaskSelectFragment)
    fun inject(taskInfoFragment: TaskInfoFragment)
    fun inject(newTaskFragment: NewTaskFragment)

    fun context(): Context
}