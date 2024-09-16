package com.ksstats.feature.teamrecordspirmarystats.data.source

data class TeamSummary(
    val teamName: String,
    val matches: Int,
    val innings: Int,
    val won: Int,
    val lost: Int,
    val tied: Int,
    val drawn: Int,
    val runs: Int,
    val wickets: Int,
    val average: Double,
    val runsPerOver: Double,
    val strikeRate: Double
)
