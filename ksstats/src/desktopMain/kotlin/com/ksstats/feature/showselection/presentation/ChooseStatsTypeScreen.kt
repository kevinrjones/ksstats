package com.ksstats.feature.showselection.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ksstats.core.presentation.StatsAppScreen
import com.ksstats.feature.showselection.domain.model.StatsSelectionItem
import com.ksstats.feature.showselection.domain.usecase.SelectionUseCases
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

fun NavGraphBuilder.chooseStatsTypeScreen(navigate: (String) -> Unit) {
    composable(
        route = StatsAppScreen.Start.name
    ) {
        val useCases: SelectionUseCases = koinInject()
        val viewModel: SelectionViewModel = viewModel {
            SelectionViewModel(useCases)
        }
        val selections = viewModel.selections.collectAsState()

        ChooseStatsTypeScreen(selections.value, onClick = {
            navigate(it)
        })

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChooseStatsTypeScreen(selections: List<StatsSelectionItem>, onClick: (String) -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
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
                            onClick = { onClick(StatsAppScreen.BattingSearch.name) },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(text = stringResource(it.text))
                        }
                    }
                }
            }
        }
    }
}

