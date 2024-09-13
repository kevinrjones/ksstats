package com.ksstats.feature.playerfieldingprimarystats.data

data class InningsByInningsFielding(
    val playerId: Int,
    val name: String,
    val team: String,
    val matchDate: Long,
    val innings: Int,
    val ground: String,
    val opponents: String,
    val dismissals: Int,
    val wicketKeepingDismissals: Int,
    val caught: Int,
    val stumpings: Int,
    val caughtKeeper: Int,
    val caughtFielder: Int,
)