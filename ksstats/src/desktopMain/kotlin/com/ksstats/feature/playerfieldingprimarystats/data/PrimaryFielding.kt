package com.ksstats.feature.playerfieldingprimarystats.data

data class PrimaryFielding(
    val playerId: Int,
    val name: String,
    val team: String,
    val opponents: String,
    val sortNamePart: String,
    val matches: Int,
    val innings: Int,
    val year: String,
    val ground: String,
    val countryName: String,
    val dismissals: Int,
    val stumped: Int,
    val caughtWk: Int,
    val caughtF: Int,
    val caught: Int,
    val wicketKeeperDismissals: Int,
    val bestDismissals: Int,
    val bestCaughtF: Int,
    val bestCaughtWk: Int,
    val bestStumpings: Int,
)

