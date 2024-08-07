package com.ksstats.feature.playerbattingprimarystats.data

data class PrimaryBatting(
    val playerId: Int,
    val name: String,
    val team: String,
    val opponents: String,
    val sortNamePart: String,
    val matches: Int,
    val innings: Int,
    val notOuts: Int,
    val runs: Int,
    val highestScore: Double,
    val average: Double,
    val strikeRate: Double,
    val battingImpact: Double,
    val hundreds: Int,
    val fifties: Int,
    val ducks: Int,
    val fours: Int,
    val sixes: Int,
    val balls: Int,
    val year: String,
    val ground: String,
    val countryName: String,
)

