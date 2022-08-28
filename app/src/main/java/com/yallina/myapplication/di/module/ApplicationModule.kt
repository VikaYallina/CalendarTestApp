package com.yallina.myapplication.di.module

import android.content.Context
import com.yallina.myapplication.MyApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: MyApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application
}