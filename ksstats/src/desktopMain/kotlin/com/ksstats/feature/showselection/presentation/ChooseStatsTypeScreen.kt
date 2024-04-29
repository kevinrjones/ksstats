package com.ksstats.feature.showselection.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ksstats.core.presentation.StatsAppScreen
import com.ksstats.feature.showselection.domain.model.StatsSelectionItem
import com.ksstats.feature.showselection.presentation.selection.SelectionViewModel
import com.ksstats.ui.theme.KSStatsTheme
import org.koin.compose.koinInject


@Composable
fun ChooseStatsTypeScreen(navigate: (String) -> Unit) {

    val viewModel: SelectionViewModel = koinInject()
    val selections = viewModel.selections.collectAsState()

    ChooseStatsTypeScreenDisplay(selections.value, navigate)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChooseStatsTypeScreenDisplay(selections: List<StatsSelectionItem>, onClick: (String) -> Unit) {

    KSStatsTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                Modifier
                    .padding(24.dp)
                    .width(300.dp)
                    .fillMaxHeight()

            ) {
                stickyHeader {
                    Text("Choose the statistics you want ", fontWeight = FontWeight.Bold)
                }
                selections.forEach {
                    item {
                        Row {
                            Button(
                                onClick = { onClick(StatsAppScreen.Batting.name) },
                                modifier = Modifier.fillMaxWidth()

                            ) {
                                Text(text = it.text)
                            }
                        }
                    }
                }
            }
        }
    }
}
