package com.ksstats.feature.playerbattingprimarystats.domain.usecase

data class PlayerBattingPrimaryStatsUseCases(
    val getBattingSummary: GetBattingSummary,
    val getSeriesAverages: GetSeriesAverages,
    val getBattingInningsByInnings: GetBattingInningsByInnings,
    val getBattingGetMatchTotals: GetMatchTotals
)

