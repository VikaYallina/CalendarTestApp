package com.yallina.myapplication.presentation.new_task_screeen.model

import org.threeten.bp.LocalDateTime


data class NewTaskPresentationModel(
    var name: String? = null,
    var description: String? = null,
    var dateStart: LocalDateTime? = null,
    var dateEnd: LocalDateTime? = null
) {
}