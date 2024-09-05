package com.ksstats.feature.playerbowlingprimarystats.domain.usecase

data class PlayerBowlingPrimaryStatsUseCases(
    val getBowlingSummary: GetBowlingSummary,
    val getBowlingInningsByInnings: GetBowlingInningsByInnings,
    val getBowlingMatchTotals: GetBowlingMatchTotals,
    val getSeriesAverages: GetSeriesAverages,
    val getGroundAverages: GetGroundAverages,
)

