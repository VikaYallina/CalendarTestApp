package com.yallina.myapplication.domain.model

import org.threeten.bp.LocalDateTime

/**
 * Domain level model that represents a task that need to be completed during some specified
 * period of time.
 * @param id unique identifier of the task
 * @param name short name of the task
 * @param description text, describing task in more detail
 * @param dateStart represents the starting time and date of the task
 * @param dateEnd represents the closing time and date of the task
 */
data class Task(
    val id: Int,
    val name: String,
    val description: String,
    val dateStart: LocalDateTime,
    val dateEnd: LocalDateTime
)