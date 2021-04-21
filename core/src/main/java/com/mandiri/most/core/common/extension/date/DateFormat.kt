package com.mandiri.most.core.common.extension.date

enum class DateFormat(val value: String) {
    TIME_STAMP_FORMAT("yyyy-MM-dd'T'HH:mm:ss"),
    DATE_FORMAT("yyyy-MM-dd"),
    TIME_FORMAT("HH:mm"),
    SHOWN_TIME("HH:mm"),
    SHOWN_DATE("dd MMM yyyy"),
    SHOWN_DATE_MONTH("dd MMM"),
    SHOWN_DATE_NUMBER("dd"),
    SHOWN_MONTH("MMM yyyy"),
    SHOWN_YEAR("yyyy"),
    SHOWN_DATE_TIME("HH:mm, dd MMM yyyy"),
    SHOWN_DATE_TIME_DELAYED_DATA("dd MMM yyyy HH:mm"),
    MULTILINE_DATE_TIME("dd MMM yyyy\nHH:mm"),
    MONTH_DATE_YEAR("MMM dd, yyyy"),
    SHORT_SHOWN_MONTH("MMM yy"),
    SHORT_SHOWN_DATE("dd MMM yy")
}