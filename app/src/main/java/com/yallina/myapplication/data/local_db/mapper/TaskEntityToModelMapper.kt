package com.yallina.myapplication.data.local_db.mapper

import com.yallina.myapplication.data.local_db.entity.TaskEntity
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.utils.EntityMapper
import org.threeten.bp.LocalDateTime

class TaskEntityToModelMapper: EntityMapper<TaskEntity, Task>() {
    override fun map(input: TaskEntity): Task {
        return Task(
            id = input.id,
            name = input.name,
            description = input.description ?: "",
            dateStart = input.dateStart ?: LocalDateTime.now(),
            dateEnd = input.dateEnd ?: LocalDateTime.now().plusHours(1)
        )
    }
}