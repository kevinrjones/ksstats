package com.ksstats.core.presentation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ksstats.core.presentation.components.KSStatsAppBar
import com.ksstats.ui.theme.KSStatsTheme


@Composable
fun KSStatsApp(navController: NavHostController = rememberNavController()) {

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    val previousBackstackEntry = navController.previousBackStackEntry
    // Get the name of the current screen
    val currentScreen = StatsAppScreens.valueOf(
        backStackEntry?.destination?.route
            ?.substringBefore("/")
            ?.substringBefore("?") ?: StatsAppScreens.Start.name
    )
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