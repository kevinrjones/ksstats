package com.ksstats.core.domain.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun createAbbreviatedEnglishDateFormat() = LocalDate.Format {
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

fun createMatchDateEnglishDateFormat() = LocalDate.Format {
    date(
        LocalDate.Format {
            dayOfMonth()
            char(' ')
            monthName(MonthNames.ENGLISH_FULL)
            chars(" ")
            year()
        }
    )
}