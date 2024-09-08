package com.ksstats.feature.playerbowlingprimarystats.domain.usecase


data class PlayerBowlingPrimaryStatsUseCases(
    val getBowlingSummary: GetBowlingSummary,
    val getBowlingInningsByInnings: GetBowlingInningsByInnings,
    val getBowlingMatchTotals: GetBowlingMatchTotals,
    val getSeriesAverages: GetSeriesAverages,
    val getGroundAverages: GetGroundAverages,
    val getByHostCountry: GetByHostCountry,
    val getBySeason: GetBySeason,
    val getByOppositionTeam: GetByOppositionTeam,
    val getByYearOfMatchStart: GetByYearOfMatchStart,
)

