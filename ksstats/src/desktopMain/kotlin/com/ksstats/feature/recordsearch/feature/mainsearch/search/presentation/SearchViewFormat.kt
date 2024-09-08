package com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation

import com.ksstats.ksstats.generated.resources.*
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.inningsByInningsList
import com.ksstats.ksstats.generated.resources.matchTotals
import com.ksstats.ksstats.generated.resources.playerSummary
import org.jetbrains.compose.resources.StringResource

enum class SearchViewFormat(val format: StringResource, val minimumRuns: Int = 100) {
    PlayerSummary(Res.string.playerSummary),
    InningsByInnings(Res.string.inningsByInningsList, 0),
    MatchTotals(Res.string.matchTotals, 0),
    SeriesAverages(Res.string.seriesAverages),
    GroundAverages(Res.string.groundAverages),
    ByHostCountry(Res.string.byHostCountry),
    ByOppositionTeam(Res.string.byOppositionTeam),
    ByYearOfMatchStart(Res.string.byYearOfMatchStart),
    BySeason(Res.string.bySeason),
}
