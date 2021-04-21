package com.mandiri.most.desktop.common.utils

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors
import java.util.stream.Stream


object Utility {
    /**
     * return true if backspace button pressed
     */
    fun disableKeyConsume(event: KeyEvent?): Boolean{
        return event?.code == KeyCode.BACK_SPACE
    }

    /**
     * return list of days withing given two dates
     * @param startDate the starting date of Calendar Selection
     * @param endDate end date of Calendar Selection
     * @return a MutableList contains all dates between start and end dates
     */
    fun getListOfDates(startDate : String?, endDate : String?): MutableList<LocalDate> {
        val endLocalDate : LocalDate = LocalDate.parse(endDate)
        val startLocalDate : LocalDate = LocalDate.parse(startDate)

        val dates: Stream<LocalDate> = startLocalDate.datesUntil(endLocalDate.plusDays(1))
        return dates.collect(Collectors.toList())
    }

    /**
     * conver string date to local date format
     * @param date the String date that need to be converted to LocalDate
     * @return returns the converted LocalDate
     */
    fun convertStringDateToLocalDate(date: String?) : LocalDate{
        val pattern = DateTimeFormatter.ofPattern("yyyy-MMM-dd")
        return LocalDate.parse(date, pattern)

    }
}