package com.ksstats.feature.playerbattingprimarystats.data

data class InningsByInningsBatting(
    val playerId: Int,
    val name: String,
    val team: String,
    val score: Int,
    val notOut: Int,
    val position: Int,
    val matchDate: Long,
    val ground: String,
    val opponents: String,
    val innings: Int,
    val strikeRate: Double,
    val sixes: Int,
    val fours: Int,
    val balls: Int,
    val minutes: Int
)