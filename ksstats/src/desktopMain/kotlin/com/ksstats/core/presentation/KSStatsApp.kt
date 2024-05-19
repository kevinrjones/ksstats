package com.ksstats.core.presentation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ksstats.core.presentation.components.KSStatsAppBar
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.app_name
import com.ksstats.ksstats.generated.resources.batting
import com.ksstats.ksstats.generated.resources.battingDetails
import com.ksstats.ui.theme.KSStatsTheme
import org.jetbrains.compose.resources.StringResource


enum class StatsAppScreen(val title: StringResource) {
    Start(title = Res.string.app_name),
    BattingSearch(title = Res.string.batting),
    BattingDetails(title = Res.string.battingDetails),
}

@Composable
fun KSStatsApp(navController: NavHostController = rememberNavController()) {

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    val previousBackstackEntry = navController.previousBackStackEntry
    // Get the name of the current screen
    val currentScreen = StatsAppScreen.valueOf(
        backStackEntry?.destination?.route
            ?.substringBefore("/")
            ?.substringBefore("?") ?: StatsAppScreen.Start.name
    )
    println(backStackEntry?.destination?.route)
    println(previousBackstackEntry?.destination?.route)
    KSStatsTheme {
        Scaffold(
            topBar = {
                KSStatsAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
        ) { innerPadding ->

            Navigation(navController, innerPadding)
        }
    }
}