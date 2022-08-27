package com.yallina.myapplication.domain.model

import org.threeten.bp.LocalDateTime


data class Task(
    val id: Int,
    val name: String,
    val description: String,
    val dateStart: LocalDateTime,
    val dateEnd: LocalDateTime
)