package com.yallina.myapplication.domain.use_case

import android.database.sqlite.SQLiteException
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.domain.repository.TasksRepository
import kotlin.jvm.Throws

class AddNewTaskUseCase(
    private val tasksRepository: TasksRepository
) {
    @Throws(SQLiteException::class)
    suspend fun execute(task: Task){
        tasksRepository.addTask(task)
    }
}