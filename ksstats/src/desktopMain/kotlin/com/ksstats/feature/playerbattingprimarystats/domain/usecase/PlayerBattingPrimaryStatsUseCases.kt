package com.ksstats.feature.playerbattingprimarystats.domain.usecase

data class PlayerBattingPrimaryStatsUseCases(
    val getBattingSummary: GetBattingSummary,
    val getSeriesAverages: GetSeriesAverages,
    val getGroundAverages: GetGroundAverages,
    val getByHostCountry: GetByHostCountry,
    val getBySeason: GetBySeason,
    val getByOppositionTeam: GetByOppositionTeam,
    val getByYearOfMatchStart: GetByYearOfMatchStart,
    val getBattingInningsByInnings: GetBattingInningsByInnings,
    val getBattingGetMatchTotals: GetMatchTotals
)

