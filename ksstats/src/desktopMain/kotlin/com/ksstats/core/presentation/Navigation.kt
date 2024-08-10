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
import com.ksstats.feature.playerbowlingprimarystats.presentation.playerBowlingSummaryScreen
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
    }

}