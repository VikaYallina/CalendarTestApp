package com.yallina.myapplication.presentation.task_info_screen.model

import com.yallina.myapplication.domain.model.Task

/**
 * Task Presentation Model that is used to present [Task] information on screen
 * @param name name of the Task
 * @param description description of the Task
 * @param dateStart starting date of the Task represented as String
 * @param dateEnd closing date of the Task represented as String
 */
data class TaskInfoPresentationModel(
    val name: String,
    val description: String,
    val dateStart: String,
    val dateEnd: String
    )