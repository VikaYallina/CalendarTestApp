package com.yallina.myapplication.presentation.new_task_screeen.model

import org.threeten.bp.LocalDateTime
import com.yallina.myapplication.domain.model.Task

/**
 * Task Presentation Model that is holds information input by user and later saved in storage
 * @param name nullable String object for [Task] name
 * @param description nullable String object for [Task] description
 * @param dateStart nullable LocalDateTime object for [Task] dateStart
 * @param dateEnd nullable LocalDateTime object for [Task] dateEnd
 */
data class NewTaskPresentationModel(
    var name: String? = null,
    var description: String? = null,
    var dateStart: LocalDateTime? = null,
    var dateEnd: LocalDateTime? = null
)
