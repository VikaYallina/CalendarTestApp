package com.yallina.myapplication.di

import android.content.Context
import com.yallina.myapplication.di.module.ApplicationModule
import com.yallina.myapplication.di.module.LocalDbModule
import com.yallina.myapplication.di.module.RepositoryModule
import com.yallina.myapplication.di.module.UseCaseModule
import com.yallina.myapplication.presentation.task_select_screen.TaskSelectFragment
import com.yallina.myapplication.presentation.task_select_screen.TaskSelectViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    ApplicationModule::class,
    LocalDbModule::class,
    RepositoryModule::class,
    UseCaseModule::class
])
@Singleton
interface AppComponent {
    fun inject(taskSelectFragment: TaskSelectFragment)
    fun inject(taskSelectViewModel: TaskSelectViewModel)

    fun context(): Context
}