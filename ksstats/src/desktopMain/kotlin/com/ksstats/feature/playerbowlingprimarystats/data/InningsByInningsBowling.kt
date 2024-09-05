package com.ksstats.feature.playerbowlingprimarystats.data

data class InningsByInningsBowling(
    val playerId: Int,
    val name: String,
    val team: String,
    val matchDate: Long,
    val ground: String,
    val opponents: String,
    val ballsPerOver: Int,
    val balls: Int,
    val maidens: Int,
    val dots: Int,
    val runs: Int,
    val wickets: Int,
    val inningsNumber: Int,
    val average: Double,
    val economy: Double,
    val seriesDate: String,
)
