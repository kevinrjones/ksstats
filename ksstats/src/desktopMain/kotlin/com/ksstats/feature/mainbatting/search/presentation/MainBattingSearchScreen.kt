package com.ksstats.feature.mainbatting.search.presentation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ksstats.feature.mainbatting.presentation.BattingRecordsViewModel
import org.koin.compose.koinInject

@Composable
fun MainBattingSearchScreen() {
    val viewModel: BattingRecordsViewModel = koinInject()

    val matchTypes = viewModel.matchTypes.collectAsState()

    MainBattingSearchScreenDisplay()
}

@Preview
@Composable
fun MainBattingSearchScreenDisplay() {
    Column(
        modifier =
        Modifier.fillMaxSize().border(width = 1.dp, color = Color.Black)
    ) {
        Row(modifier = Modifier.weight(0.125f)) {
            Text("Match type etc")
        }
        Row(modifier = Modifier.weight(0.125f)) {
            Text("Teams")
        }
        Row(modifier = Modifier.weight(0.125f)) {
            Text("Venue")
        }
        Row(modifier = Modifier.weight(0.125f)) {
            Text("Countries/Grounds")
        }
        Row(modifier = Modifier.weight(0.125f)) {
            Text("Dates")
        }
        Row(modifier = Modifier.weight(0.125f)) {
            Text("Results")
        }
        Row(modifier = Modifier.weight(0.125f)) {
            Text("Format")
        }
        Row(modifier = Modifier.weight(0.125f)) {
            Text("Buttons")
        }
    }
}