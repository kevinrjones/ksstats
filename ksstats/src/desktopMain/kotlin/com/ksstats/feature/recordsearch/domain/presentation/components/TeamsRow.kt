package com.ksstats.feature.recordsearch.domain.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ksstats.core.presentation.components.AppDropDownParams
import com.ksstats.core.presentation.components.LargeDropdownMenu
import com.ksstats.core.presentation.components.LargeDropdownMenuItem
import com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation.MainSearchEvent
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.opponentsLabel
import com.ksstats.ksstats.generated.resources.teamsLabel
import org.jetbrains.compose.resources.stringResource

@Composable
fun TeamsRow(
    teamParams: AppDropDownParams,
    oppositionParams: AppDropDownParams,
    onBattingEvent: (MainSearchEvent) -> Unit = {},
) {
    Row(
        modifier = Modifier.padding(bottom = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .padding(start = 5.dp, end = 5.dp)
        ) {
            var selectedIndex by remember(teamParams.selectedOption) {
                val ndx = teamParams.options.indexOf(teamParams.selectedOption)
                mutableStateOf(ndx)
            }
            LargeDropdownMenu(
                label = stringResource(Res.string.teamsLabel),
                items = teamParams.options,
                selectedIndex = selectedIndex,
                selectedItemToString = {
                    it.value
                },
                onItemSelected = { index, option ->
                    selectedIndex = index
                    onBattingEvent(MainSearchEvent.TeamSelectionEvent(option))
                },
                drawItem = { item, selected, itemEnabled, onClick ->
                    LargeDropdownMenuItem(
                        text = item.value,
                        selected = selected,
                        enabled = itemEnabled,
                        onClick = onClick,
                    )
                },
                filterItem = { item, filter ->
                    item.value.lowercase().contains(filter.lowercase())
                }
            )
        }
        Column(
            modifier = Modifier
                .width(300.dp)
                .padding(start = 5.dp, end = 5.dp)
        ) {
            var selectedIndex by remember(oppositionParams.selectedOption) {
                val ndx = oppositionParams.options.indexOf(oppositionParams.selectedOption)
                mutableStateOf(ndx)
            }
            LargeDropdownMenu(
                label = stringResource(Res.string.opponentsLabel),
                items = oppositionParams.options,
                selectedIndex = selectedIndex,
                selectedItemToString = {
                    it.value
                },
                onItemSelected = { index, option ->
                    selectedIndex = index
                    onBattingEvent(MainSearchEvent.OpponentSelectionEvent(option))
                },
                drawItem = { item, selected, itemEnabled, onClick ->
                    LargeDropdownMenuItem(
                        text = item.value,
                        selected = selected,
                        enabled = itemEnabled,
                        onClick = onClick,
                    )
                },
                filterItem = { item, filter ->
                    item.value.lowercase().contains(filter.lowercase())
                }
            )
        }
    }
}