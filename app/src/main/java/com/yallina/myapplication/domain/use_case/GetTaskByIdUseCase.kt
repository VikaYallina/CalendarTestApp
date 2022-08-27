package com.yallina.myapplication.domain.use_case

import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow

class GetTaskByIdUseCase(
    private val tasksRepository: TasksRepository
) {
    fun execute(id: Int): Flow<Task> {
        return tasksRepository.getTaskById(id)
    }
}