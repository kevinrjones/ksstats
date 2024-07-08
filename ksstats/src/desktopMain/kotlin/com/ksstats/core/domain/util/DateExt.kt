package com.ksstats.core.domain.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun createEnglishDateFormat() = LocalDate.Format {
    date(
        LocalDate.Format {
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            dayOfMonth()
            chars(" ")
            year()
        }
    )
}