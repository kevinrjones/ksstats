package com.ksstats.feature.teamrecordspirmarystats.domain.usecase

data class TeamPrimaryStatsUseCases(
    val getTeamSummary: GetTeamSummary,
    val getTeamInningsByInnings: GetTeamInningsByInnings,
    val getMatchTotals: GetMatchTotals,
    val getMatchResults: GetMatchResults,
    val getSeriesRecords: GetSeriesRecords,
)

