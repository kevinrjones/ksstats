package com.ksstats.feature.summary.util

import com.ksstats.core.types.EpochSeconds
import com.ksstats.feature.summary.domain.model.SummaryResult
import com.ksstats.shared.fromSeconds
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun SummaryResult.buildSummary(startDate: EpochSeconds, endDate: EpochSeconds): String {
    val format = LocalDate.Format {
        date(
            LocalDate.Format {
                monthName(MonthNames.ENGLISH_ABBREVIATED)
                char(' ')
                dayOfMonth()
                char(' ')
                year()
            }
        )
    }

    val sd = startDate.value.fromSeconds().format(format)
    val ed = endDate.value.fromSeconds().format(format)

    val teamsString = if (this.team.lowercase() != "all" || this.opponents.lowercase() != "all") {
        "${this.team} vs ${this.opponents}"
    } else {
        "All Teams"
    }

    val competitionString = if (this.matchType == this.competition) {
        this.matchType.value
    } else {
        "${this.matchType.value} (${this.competition})"
    }


    val hostCountryString = if (this.hostCountry.lowercase() == "all") {
        ""
    } else {
        " in ${this.hostCountry}"
    }

    val groundString = if (this.ground.lowercase() == "all") {
        ""
    } else {
        " at Ground ${this.ground}"
    }


    return "$teamsString competing in ${competitionString}${hostCountryString}${groundString} between $sd and $ed"
}
