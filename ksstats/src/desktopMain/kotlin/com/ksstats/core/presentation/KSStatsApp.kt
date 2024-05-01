package com.ksstats.core.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ksstats.core.presentation.components.KSStatsAppBar
import com.ksstats.feature.mainbatting.search.presentation.MainBattingSearchScreen
import com.ksstats.feature.showselection.presentation.ChooseStatsTypeScreen
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.app_name
import com.ksstats.ksstats.generated.resources.batting
import org.jetbrains.compose.resources.StringResource


enum class StatsAppScreen(val title: StringResource) {
    Start(title = Res.string.app_name),
    Batting(title = Res.string.batting),
}

@Composable
fun KSStatsApp(navController: NavHostController = rememberNavController()) {

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = StatsAppScreen.valueOf(
        backStackEntry?.destination?.route ?: StatsAppScreen.Start.name
    )
    Scaffold(
        topBar = {
            KSStatsAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->


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
            composable(route = StatsAppScreen.Batting.name) {
                MainBattingSearchScreen()
            }
        }
    }
}