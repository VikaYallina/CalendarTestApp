package com.yallina.myapplication.utils

import org.threeten.bp.format.DateTimeFormatter

object dateTimeFormatter {
    val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm:ss")
}