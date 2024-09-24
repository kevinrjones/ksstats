package com.ksstats.feature.teamrecordspirmarystats.data.source

import java.time.LocalDate

data class MatchResults(
    val team: String,
    val opponents: String,
    val victoryType: Int,
    val howMuch: Int,
    val matchStartDate: String,
    val matchStartDateAsOffset: Long,
    val resultString: String,
    val knownAs: String,
    val caId: String,
    val teamId: Int,
    val opponentsId: Int,
    val whoWonId: Int,
    val tossTeamId: Int,
)