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
import com.ksstats.ksstats.generated.resources.countryLabel
import com.ksstats.ksstats.generated.resources.groundLabel
import org.jetbrains.compose.resources.stringResource

@Composable
fun CountriesGroundsRow(
    groundsParams: AppDropDownParams,
    countriesParams: AppDropDownParams,
    onBattingEvent: (MainSearchEvent) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .padding(start = 5.dp, end = 5.dp)
        ) {
            var selectedIndex by remember(countriesParams.selectedOption) {
                val ndx = countriesParams.options.indexOf( countriesParams.selectedOption )
                mutableStateOf(ndx)
            }
            LargeDropdownMenu(
                label = stringResource(Res.string.countryLabel),
                items = countriesParams.options,
                selectedIndex = selectedIndex,
                selectedItemToString = {
                    it.value
                },
                onItemSelected = { index, option ->
                    selectedIndex = index
                    onBattingEvent(MainSearchEvent.CountrySelectionEvent(option))
                },
                drawItem = { item, selected, itemEnabled, onClick ->
                    LargeDropdownMenuItem(
                        text = item.value,
                        selected = selected,
                        enabled = itemEnabled,
                        onClick = onClick,
                    )
                },
                filterItem = {item, filter ->
                    item.value.lowercase().contains(filter.lowercase())
                }
            )
        }
        Column(
            modifier = Modifier
                .width(300.dp)
                .padding(start = 5.dp, end = 5.dp)
        ) {
            var selectedIndex by remember(groundsParams.selectedOption) {
                val ndx = groundsParams.options.indexOf( groundsParams.selectedOption )
                mutableStateOf(ndx)
            }
            LargeDropdownMenu(
                label = stringResource(Res.string.groundLabel),
                items = groundsParams.options,
                selectedIndex = selectedIndex,
                selectedItemToString = {
                    it.value
                },
                onItemSelected = { index, option ->
                    selectedIndex = index
                    onBattingEvent(MainSearchEvent.GroundSelectionEvent(option))
                },
                drawItem = { item, selected, itemEnabled, onClick ->
                    LargeDropdownMenuItem(
                        text = item.value,
                        selected = selected,
                        enabled = itemEnabled,
                        onClick = onClick,
                    )
                },
                filterItem = {item, filter ->
                    item.value.lowercase().contains(filter.lowercase())
                }
            )
        }
    }
}