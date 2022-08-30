package com.yallina.myapplication.presentation.task_select_screen.model

import com.yallina.myapplication.utils.MyDateTimeFormatter
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Holds a [LocalDate] object and is used to display time on UI
 * @param date [LocalDate] object
 * @param defaultDateFormatter [DateTimeFormatter] can be specified to parse date into String
 */
data class LocalDatePresentationModel(
    val date: LocalDate,
    private val defaultDateFormatter: DateTimeFormatter = MyDateTimeFormatter.formatter
) {
    /**
     * Convert [LocalDate] object to milliseconds with system default TimeZone
     */
    fun toMillis(): Long {
        val zdt = ZonedDateTime.of(date.atStartOfDay(), ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli()
    }

    override fun toString(): String {
        return date.format(defaultDateFormatter)
    }

    /**
     * Format [LocalDate] field using specified [DateTimeFormatter]
     */
    fun toFormattedString(formatter: DateTimeFormatter): String {
        return date.format(formatter)
    }
}