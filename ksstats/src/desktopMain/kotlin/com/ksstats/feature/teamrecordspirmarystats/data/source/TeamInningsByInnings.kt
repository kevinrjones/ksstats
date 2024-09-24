package com.ksstats.feature.teamrecordspirmarystats.data.source

data class TeamInningsByInnings(
    val team: String,
    val opponents: String,
    val innings: Int,
    val score: Int,
    val overs:String,
    val runsPerOver: Double,
    val result: String,
    val ground: String,
    val startDate: String,
    val wickets: Int
) {
}