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
import com.ksstats.feature.mainbatting.search.presentation.MainBattingSearchScreen
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
        composable(route = StatsAppScreen.BattingSearch.name) {
            val matchType = navController.currentBackStackEntry?.savedStateHandle?.get<String>("matchType")
            MainBattingSearchScreen(
                matchType = matchType,
                navigate = {
                    navController.navigate(it)
                })
        }
        composable(
            route = StatsAppScreen.BattingDetails.name +
                    "?matchType={matchType}" +
                    "&matchSubType={matchSubType}" +
                    "&teamId={teamId}" +
                    "&opponentsId={opponentsId}" +
                    "&groundId={groundId}" +
                    "&hostCountryId={hostCountryId}" +
                    "&venue={venue}" +
                    "&sortOrder={sortOrder}" +
                    "&sortDirection={sortDirection}" +
                    "&startDate={startDate}" +
                    "&endDate={endDate}" +
                    "&result={result}" +
                    "&limit={limit}" +
                    "&startRow={startRow}" +
                    "&pageSize={pageSize}",
            arguments = listOf(
                navArgument(name = "matchType") {
                    type = NavType.StringType
                    defaultValue = "t"
                },
                navArgument(name = "matchSubType") {
                    type = NavType.StringType
                    defaultValue = "t"
                },
                navArgument(name = "teamId") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument(name = "opponentsId") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument(name = "groundId") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument(name = "hostCountryId") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument(name = "venue") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument(name = "sortOrder") {
                    type = NavType.IntType
                    defaultValue = 4
                },
                navArgument(name = "sortDirection") {
                    type = NavType.StringType
                    defaultValue = "DESC"
                },
                navArgument(name = "startDate") {
                    type = NavType.LongType
                    defaultValue = -8520336000
                },
                navArgument(name = "endDate") {
                    type = NavType.LongType
                    defaultValue = 253402214400
                },
                navArgument(name = "result") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument(name = "limit") {
                    type = NavType.IntType
                    defaultValue = 100
                },
                navArgument(name = "startRow") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument(name = "pageSize") {
                    type = NavType.IntType
                    defaultValue = 50
                },
            )
        ) {
//                    val matchType = it.arguments?.getString("matchType") ?: "t"
//                    navController.previousBackStackEntry?.savedStateHandle?.set("matchType", matchType)
            BattingDetailsScreen(
                matchType = it.arguments?.getString("matchType") ?: "t",
                matchSubType = it.arguments?.getString("matchSubType") ?: "t",
                teamId = it.arguments?.getInt("teamId"),
                opponentsId = it.arguments?.getInt("opponentsId"),
                groundId = it.arguments?.getInt("groundId"),
                hostCountryId = it.arguments?.getInt("hostCountryId"),
                venue = it.arguments?.getInt("venue"),
                sortOrder = it.arguments?.getInt("sortOrder"),
                sortDirection = it.arguments?.getString("sortDirection"),
                startDate = it.arguments?.getLong("startDate"),
                endDate = it.arguments?.getLong("endDate"),
                result = it.arguments?.getInt("result"),
                limit = it.arguments?.getInt("limit"),
                startRow = it.arguments?.getInt("startRow"),
                pageSize = it.arguments?.getInt("pageSize"),

                )

        }
    }

}