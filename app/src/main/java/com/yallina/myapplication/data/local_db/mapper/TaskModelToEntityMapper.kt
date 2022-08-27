package com.yallina.myapplication.data.local_db.mapper

import com.yallina.myapplication.data.local_db.entity.TaskEntity
import com.yallina.myapplication.domain.model.Task
import com.yallina.myapplication.utils.EntityMapper

class TaskModelToEntityMapper: EntityMapper<Task, TaskEntity>() {
    override fun map(input: Task): TaskEntity {
        return TaskEntity(
            id = input.id,
            name = input.name,
            description = input.description,
            dateStart = input.dateStart,
            dateEnd = input.dateEnd
        )
    }
}