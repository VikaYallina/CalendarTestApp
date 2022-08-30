package com.yallina.myapplication.domain.use_case

import com.yallina.myapplication.domain.repository.TasksRepository
import com.yallina.myapplication.domain.model.Result
import java.io.InputStream

class InitDataFromFileUseCase(
    private val tasksRepository: TasksRepository
) {
    suspend fun execute(inputStream: InputStream): Result<Nothing>{
        return tasksRepository.initTasks(inputStream)
    }
}