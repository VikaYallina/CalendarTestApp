package com.yallina.myapplication.data.local_file.task.model

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime
import java.io.Serializable
import com.yallina.myapplication.domain.model.Task

/**
 * [Task] data entity that is used in serialization process
 * @param name represents a [Task] name
 * @param description represents a [Task] description
 * @param dateStart represents a [Task]'s starting date and time
 * @param dateEnd represents a [Task]'s closing date and time
 */
data class SerializedTask(
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("date_start") val dateStart: LocalDateTime? = null,
    @SerializedName("date_end") val dateEnd: LocalDateTime? = null,
) : Serializable

