package com.yallina.myapplication.domain.use_case

import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow

class GetAllTasksUseCase(
    private val tasksRepository: TasksRepository
) {
    fun execute(): Flow<List<Task>>{
        return tasksRepository.getAllTasks()
    }
}