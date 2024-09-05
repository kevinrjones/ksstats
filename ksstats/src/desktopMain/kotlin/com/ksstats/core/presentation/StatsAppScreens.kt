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
    PlayersSearch(title = Res.string.players),
    PartnershipSearch(title = Res.string.partnerships),
    FieldingSearch(title = Res.string.fielding),
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
}