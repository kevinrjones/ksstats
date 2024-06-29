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
    BattingInningsByInningsSummary(title = Res.string.playerSummary),
    BattingMatchTotalsSummary(title = Res.string.playerSummary),
    BattingSeriesAveragesSummary(title = Res.string.playerSummary),
    BattingGroundAveragesSummary(title = Res.string.playerSummary),
    BattingByHostCuntrySummary(title = Res.string.playerSummary),
    BattingByOppositionTeamSummary(title = Res.string.playerSummary),
    BattingByYearSummary(title = Res.string.playerSummary),
    BattingBySeasonSummary(title = Res.string.playerSummary),
    BowlingPlayerSummary(title = Res.string.playerSummary),
}