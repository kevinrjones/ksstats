package com.ksstats.app

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.ksstats.core.presentation.KSStatsApp
import com.ksstats.di.appModule
import com.ksstats.feature.showselection.presentation.ChooseStatsTypeScreen
import org.koin.compose.KoinApplication
import org.koin.core.logger.Level


fun main() = application {
    val state: WindowState = rememberWindowState()

    state.size = DpSize(900.dp, 800.dp)
    state.position = WindowPosition(Alignment.Center)


    KoinApplication(
        application = {
            modules(appModule())
            printLogger(Level.DEBUG)
        }
    ) {

        Window(
            onCloseRequest = ::exitApplication,
            title = "Order Form ",
            state = state,
        ) {
            // todo: add a version table to the database and a check against the application version (loaded from where?)

            KSStatsApp()
        }
    }
}