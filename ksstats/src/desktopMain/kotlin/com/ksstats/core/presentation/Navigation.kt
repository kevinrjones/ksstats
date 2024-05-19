package com.ksstats.core.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ksstats.feature.mainbatting.battingrecords.presentation.BattingDetailsScreen
import com.ksstats.feature.mainbatting.battingrecords.presentation.battingDetailsScreen
import com.ksstats.feature.mainbatting.search.presentation.MainBattingSearchScreen
import com.ksstats.feature.mainbatting.search.presentation.mainBattingSearchScreenScreen
import com.ksstats.feature.showselection.presentation.ChooseStatsTypeScreen

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

        composable(route = StatsAppScreen.Start.name) {
            ChooseStatsTypeScreen(navigate = {
                navController.navigate(it)
            })
        }
        composable(
            route = StatsAppScreen.BattingSearch.name +
                    "?matchType={matchType}",
            arguments = listOf(
                navArgument(name = "matchType") {
                    type = NavType.StringType
                    defaultValue = "t"
                    nullable = true
                },
            )
        ) {
            val matchType = navController.currentBackStackEntry?.savedStateHandle?.get<String>("matchType")
            val mt1 = navController.currentBackStackEntry?.arguments?.getString("matchType")
            val mt = it.arguments?.getString("matchType")
            MainBattingSearchScreen(
                matchType = matchType,
                navigate = {
                    navController.navigate(it)
                })
        }
//        mainBattingSearchScreenScreen(navigate = { navController.navigate(it) })
        battingDetailsScreen()
    }

}