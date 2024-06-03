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
import com.ksstats.feature.battingrecordsdisplay.presentation.battingDetailsScreen
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.search.presentation.mainBattingSearchScreen
import com.ksstats.feature.showselection.presentation.chooseStatsTypeScreen

@Composable
fun Navigation(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = StatsAppScreen.Start.name,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
    ) {

        chooseStatsTypeScreen(navigate = {
            navController.navigate(it) {
                popUpTo(StatsAppScreen.Start.name) {
                    saveState = true
                }
                restoreState = true
            }

        })
        mainBattingSearchScreen(navigate = {


            navController.navigate(it) {
                popUpTo(StatsAppScreen.BattingSearch.name) {
                    saveState = true
                }
                restoreState = true
            }
        })
        battingDetailsScreen(navigate = {navController.navigate(it) })
    }

}