package com.ksstats.feature.playerfieldingprimarystats.domain.usecase

data class PlayerFieldingPrimaryStatsUseCases(
    val getFieldingSummary: GetFieldingSummary,
    val getSeriesAverages: GetSeriesAverages,
    val getGroundAverages: GetGroundAverages,
    val getByHostCountry: GetByHostCountry,
    val getBySeason: GetBySeason,
    val getByOppositionTeam: GetByOppositionTeam,
    val getByYearOfMatchStart: GetByYearOfMatchStart,
    val getFieldingInningsByInnings: GetFieldingInningsByInnings,
    val getFieldingGetMatchTotals: GetMatchTotals
)

