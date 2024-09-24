package com.ksstats.core.presentation

import com.ksstats.ksstats.generated.resources.*
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.app_name
import com.ksstats.ksstats.generated.resources.batting
import com.ksstats.ksstats.generated.resources.bowling
import org.jetbrains.compose.resources.StringResource

enum class StatsAppScreens(val title: StringResource) {
    Start(title = Res.string.app_name),
    BattingSearch(title = Res.string.batting),
    BowlingSearch(title = Res.string.bowling),
    FieldingSearch(title = Res.string.fielding),
    TeamSearch(title = Res.string.teamsLabel),
    PartnershipSearch(title = Res.string.partnerships),
    PlayersSearch(title = Res.string.players),
    OfficialsSearch(title = Res.string.officials),

    BattingPlayerSummary(title = Res.string.playerSummary),
    BattingInningsByInnings(title = Res.string.playerSummary),
    BattingMatchTotals(title = Res.string.playerSummary),
    BattingSeriesAverages(title = Res.string.playerSummary),
    BattingGroundAverages(title = Res.string.playerSummary),
    BattingByHostCountry(title = Res.string.playerSummary),
    BattingByOppositionTeam(title = Res.string.playerSummary),
    BattingByYear(title = Res.string.playerSummary),
    BattingBySeason(title = Res.string.playerSummary),
    BowlingPlayerSummary(title = Res.string.playerSummary),
    BowlingInningsByInnings(title = Res.string.playerSummary),
    BowlingMatchTotals(title = Res.string.playerSummary),
    BowlingSeriesAverages(title = Res.string.playerSummary),
    BowlingGroundAverages(title = Res.string.playerSummary),
    BowlingByHostCountry(title = Res.string.playerSummary),
    BowlingByOppositionTeam(title = Res.string.playerSummary),
    BowlingByYear(title = Res.string.playerSummary),
    BowlingBySeason(title = Res.string.playerSummary),
    FieldingPlayerSummary(title = Res.string.playerSummary),
    FieldingInningsByInnings(title = Res.string.playerSummary),
    FieldingMatchTotals(title = Res.string.playerSummary),
    FieldingSeriesAverages(title = Res.string.playerSummary),
    FieldingGroundAverages(title = Res.string.playerSummary),
    FieldingByHostCountry(title = Res.string.playerSummary),
    FieldingByOppositionTeam(title = Res.string.playerSummary),
    FieldingByYear(title = Res.string.playerSummary),
    FieldingBySeason(title = Res.string.playerSummary),
    TeamSummary(title = Res.string.teamSummary),
    TeamInningsByInnings(title = Res.string.inningsByInningsScreenTitle),
    TeamMatchTotalsScreen(title = Res.string.matchTotals),
    MatchResults(title = Res.string.matchResults),

}