package com.ksstats.core.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ksstats.feature.playerbattingprimarystats.presentation.playerBattingInningsByInningsScreen
import com.ksstats.feature.playerbattingprimarystats.presentation.playerBattingSummaryScreen
import com.ksstats.feature.playerbowlingprimarystats.presentation.playerBowlingInningsByInningsScreen
import com.ksstats.feature.playerbowlingprimarystats.presentation.playerBowlingSummaryScreen
import com.ksstats.feature.playerfieldingprimarystats.presentation.playerFieldingInningsByInningsScreen
import com.ksstats.feature.playerfieldingprimarystats.presentation.playerFieldingSummaryScreen
import com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation.mainSearchScreen
import com.ksstats.feature.recordsearch.feature.mainsearch.search.utils.MainSearchType
import com.ksstats.feature.showselection.presentation.chooseStatsTypeScreen
import com.ksstats.ksstats.generated.resources.*
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.minimumRunsLabel
import com.ksstats.ksstats.generated.resources.minimumWicketsLabel

@Composable
fun Navigation(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = StatsAppScreens.Start.name,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
    ) {

        chooseStatsTypeScreen(navigate = {
            navController.navigate(it) {
                popUpTo(StatsAppScreens.Start.name) {
                    saveState = true
                }
                restoreState = true
            }

        })
        mainSearchScreen(MainSearchType.Batting,
            limitLabel = Res.string.minimumRunsLabel,
            navigate = {
                navController.navigate(it) {
                    popUpTo(StatsAppScreens.BattingSearch.name) {
                        saveState = true
                    }
                    restoreState = true
                }
            })
        mainSearchScreen(MainSearchType.Bowling,
            limitLabel = Res.string.minimumWicketsLabel,
            navigate = {
                navController.navigate(it) {
                    popUpTo(StatsAppScreens.BowlingSearch.name) {
                        saveState = true
                    }
                    restoreState = true
                }
            })
        mainSearchScreen(MainSearchType.Fielding,
            limitLabel = Res.string.minimumDismissalsLabel,
            navigate = {
                navController.navigate(it) {
                    popUpTo(StatsAppScreens.FieldingSearch.name) {
                        saveState = true
                    }
                    restoreState = true
                }
            })
        playerBattingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BattingPlayerSummary,
            title = Res.string.playerBattingSummaryTitle
        )
        playerBattingInningsByInningsScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BattingInningsByInnings,
            title = Res.string.inningsByInningsScreenTitle
        )
        playerBattingInningsByInningsScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BattingMatchTotals,
            title = Res.string.matchScreenTitle
        )
        playerBattingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BattingSeriesAverages,
            title = Res.string.playerSeriesAveragesTitle
        )
        playerBattingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BattingGroundAverages,
            title = Res.string.playerGroundAveragesTitle
        )
        playerBattingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BattingByHostCountry,
            title = Res.string.playerByHostCountryTitle
        )
        playerBattingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BattingByOppositionTeam,
            title = Res.string.playerByOppositionTeamTitle
        )
        playerBattingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BattingByYear,
            title = Res.string.playerByYearOfMatchStartTitle
        )
        playerBattingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BattingBySeason,
            title = Res.string.playerBySeasonTitle
        )
        playerBowlingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BowlingPlayerSummary,
            title = Res.string.playerBowlingSummaryTitle
        )
        playerBowlingInningsByInningsScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BowlingInningsByInnings,
            title = Res.string.inningsByInningsScreenTitle
        )
        playerBowlingInningsByInningsScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BowlingMatchTotals,
            title = Res.string.matchScreenTitle
        )
        playerBowlingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BowlingSeriesAverages,
            title = Res.string.playerSeriesAveragesTitle
        )
        playerBowlingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BowlingGroundAverages,
            title = Res.string.playerGroundAveragesTitle
        )
        playerBowlingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BowlingByHostCountry,
            title = Res.string.playerByHostCountryTitle
        )
        playerBowlingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BowlingByOppositionTeam,
            title = Res.string.playerByOppositionTeamTitle
        )
        playerBowlingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BowlingByYear,
            title = Res.string.playerByYearOfMatchStartTitle
        )
        playerBowlingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.BowlingBySeason,
            title = Res.string.playerBySeasonTitle
        )
        playerFieldingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.FieldingPlayerSummary,
            title = Res.string.playerFieldingSummaryTitle
        )
        playerFieldingInningsByInningsScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.FieldingInningsByInnings,
            title = Res.string.inningsByInningsScreenTitle
        )
        playerFieldingInningsByInningsScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.FieldingMatchTotals,
            title = Res.string.matchScreenTitle
        )
        playerFieldingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.FieldingSeriesAverages,
            title = Res.string.playerSeriesAveragesTitle
        )
        playerFieldingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.FieldingGroundAverages,
            title = Res.string.playerGroundAveragesTitle
        )
        playerFieldingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.FieldingByHostCountry,
            title = Res.string.playerByHostCountryTitle
        )
        playerFieldingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.FieldingByOppositionTeam,
            title = Res.string.playerByOppositionTeamTitle
        )
        playerFieldingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.FieldingByYear,
            title = Res.string.playerByYearOfMatchStartTitle
        )
        playerFieldingSummaryScreen(
            navigate = { navController.navigate(it) },
            screen = StatsAppScreens.FieldingBySeason,
            title = Res.string.playerBySeasonTitle
        )

    }

}