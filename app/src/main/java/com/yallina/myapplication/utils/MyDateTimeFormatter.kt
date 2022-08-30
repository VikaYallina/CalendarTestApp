package com.yallina.myapplication.utils

import org.threeten.bp.format.DateTimeFormatter

object MyDateTimeFormatter {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm:ss")
    val isoLocalDateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
}