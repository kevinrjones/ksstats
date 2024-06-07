package com.ksstats.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.ksstats.core.presentation.KSStatsApp
import com.ksstats.di.appModule
import org.koin.compose.KoinApplication
import org.koin.core.logger.Level
import java.awt.Dimension
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths


fun main() = application {
    val state: WindowState = rememberWindowState()

    state.size = DpSize(1200.dp, 1200.dp)
    state.position = WindowPosition(Alignment.Center)

    val home = System.getProperty("user.home")
    val ksstats = ".ksstats"
    val separator = File.separatorChar

    val databaseDirectory = "${home}$separator${ksstats}$separator"

    val connectionString = "jdbc:sqlite:${databaseDirectory}cricket.sqlite"

    KoinApplication(
        application = {
            modules(appModule(connectionString))
            printLogger(Level.DEBUG)
        }
    ) {

        Window(
            onCloseRequest = ::exitApplication,
            title = "Order Form ",
            state = state,
        ) {
            // todo: add a version table to the database and a check against the application version (loaded from where?)

            window.minimumSize = Dimension(800, 800)

            val expectedFileName = "${databaseDirectory}cricket.sqlite"

            if (!Files.exists(Paths.get(expectedFileName))) {
                DatabaseNotFoundDialog(expectedFileName)
            } else {
                KSStatsApp()
            }

        }
    }
}

@Composable
private fun ApplicationScope.DatabaseNotFoundDialog(expectedFileName: String) {
    BasicAlertDialog(onDismissRequest = { }) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "I'm expecting to find a database file at $expectedFileName and this file does not exist",
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Please follow the instructions at foo.com on how to intall this file",
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        exitApplication()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}