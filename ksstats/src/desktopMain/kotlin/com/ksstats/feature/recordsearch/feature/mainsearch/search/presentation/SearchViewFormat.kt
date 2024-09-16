package com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation

import com.ksstats.ksstats.generated.resources.*
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.inningsByInningsList
import com.ksstats.ksstats.generated.resources.matchTotals
import com.ksstats.ksstats.generated.resources.playerSummary
import org.jetbrains.compose.resources.StringResource

enum class SearchViewFormat(val format: StringResource) {
    PlayerSummary(Res.string.playerSummary),
    TeamSummary(Res.string.teamSummary),
    InningsByInnings(Res.string.inningsByInningsList),
    MatchTotals(Res.string.matchTotals),
    MatchResults(Res.string.matchResults),
    SeriesAverages(Res.string.seriesAverages),
    GroundAverages(Res.string.groundAverages),
    ByHostCountry(Res.string.byHostCountry),
    ByOppositionTeam(Res.string.byOppositionTeam),
    ByYearOfMatchStart(Res.string.byYearOfMatchStart),
    BySeason(Res.string.bySeason),
    OverallExtras(Res.string.overallExtras),
    ExtrasByInnings(Res.string.extrasByInnings),
    HighestTargetChased(Res.string.highestTargetChased),
    SuccessfullyDefendedLowestTarget(Res.string.successfullyDefendedLowestTarget),
    SuccessfullyDefendedInUnReducedMatch(Res.string.successfullyDefendedInUnReducedMatch),
}
