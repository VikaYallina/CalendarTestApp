package com.yallina.myapplication

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.yallina.myapplication.di.AppComponent
import com.yallina.myapplication.di.DaggerAppComponent
import com.yallina.myapplication.di.module.*

class MyApplication: Application() {

    private var applicationComponent: AppComponent? = null

    lateinit var injector: AppComponent
        private set
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        INSTANCE = this
        injector = initializeInjector()
    }

    private fun initializeInjector(): AppComponent{
        return DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .localDbModule(LocalDbModule())
            .repositoryModule(RepositoryModule())
            .useCaseModule(UseCaseModule())
            .assetReaderModule(AssetReaderModule())
            .viewModelFactoryModule(ViewModelFactoryModule())
            .build()
    }

    fun getApplicationComponent(): AppComponent{
        return applicationComponent ?: initializeInjector()
    }

    companion object{
        private lateinit var INSTANCE: MyApplication

        @JvmStatic
        fun get(): MyApplication = INSTANCE
    }
}