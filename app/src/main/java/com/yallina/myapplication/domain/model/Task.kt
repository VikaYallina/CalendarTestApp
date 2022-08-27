package com.yallina.myapplication.domain.model

import java.time.LocalDateTime

data class Task(
    val id: Int,
    val name: String,
    val description: String,
    val dateStart: LocalDateTime,
    val dateEnd: LocalDateTime
)