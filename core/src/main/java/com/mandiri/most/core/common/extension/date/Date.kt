package com.mandiri.most.core.common.extension.date

import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(format: DateFormat): String {
    return buildDateFormat(format).format(this)
}

fun Date.add(calendarField: Int, number: Int): Date {
    return Calendar.getInstance().apply {
        time = this@add
        add(calendarField, number)
    }.time
}

fun String.toDate(format: DateFormat): Date {
    return buildDateFormat(format).parse(this)
}

private fun buildDateFormat(format: DateFormat) = when (format) {
    DateFormat.TIME_STAMP_FORMAT -> buildUtcDateFormat(format)
    else -> buildSimpleDateFormat(format)
}

private fun buildSimpleDateFormat(format: DateFormat) =
    SimpleDateFormat(format.value, Locale.getDefault())

private fun buildUtcDateFormat(format: DateFormat) =
    SimpleDateFormat(format.value, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
