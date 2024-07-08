package com.ksstats.feature.playerbattingprimarystats.domain.usecase

data class PlayerBattingPrimaryStatsUseCases(
    val getBattingSummary: GetBattingSummary,
    val getBattingInningaByInnings: GetBattingInningsByInnings
)

