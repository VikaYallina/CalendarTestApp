package com.yallina.myapplication.di.module

import com.yallina.myapplication.data.local_file.base.AssetReader
import com.yallina.myapplication.data.local_file.task.TaskAssetReaderImpl
import com.yallina.myapplication.data.local_file.task.model.SerializedTask
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AssetReaderModule {
    @Singleton
    @Provides
    fun provideAssetReader(): AssetReader<SerializedTask> = TaskAssetReaderImpl()
}