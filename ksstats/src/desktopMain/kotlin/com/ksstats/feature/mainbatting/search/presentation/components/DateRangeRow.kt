package com.ksstats.feature.mainbatting.search.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ksstats.core.presentation.components.AppDropDownParams
import com.ksstats.core.presentation.components.LargeDropdownMenu
import com.ksstats.core.presentation.components.LargeDropdownMenuItem
import com.ksstats.core.presentation.components.PopupDatePicker
import com.ksstats.feature.mainbatting.presentation.BattingSearchEvent
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.datesLabel
import com.ksstats.ksstats.generated.resources.seriesDateLabel
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource

@Composable
fun DateRangeRow(
    seriesDatesParams: AppDropDownParams,
    startDateLabel: String,
    startDate: LocalDate,
    endDateLabel: String,
    endDate: LocalDate,
    onBattingEvent: (BattingSearchEvent) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(start = 5.dp, bottom = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(start = 5.dp, end = 25.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.datesLabel)
            )
        }
        Column(
            modifier = Modifier
                .width(300.dp)
                .padding(start = 5.dp, end = 5.dp)
        ) {
            PopupDatePicker(label = startDateLabel, date = startDate) {
                onBattingEvent(BattingSearchEvent.StartDateChangedEvent(it))
            }
        }
        Column(
            modifier = Modifier
                .width(300.dp)
                .padding(start = 5.dp, end = 5.dp)
        ) {
            PopupDatePicker(label = endDateLabel, date = endDate) {
                onBattingEvent(BattingSearchEvent.EndDateChangedEvent(it))
            }
        }

        Column(
            modifier = Modifier
                .width(300.dp)
                .padding(start = 5.dp, end = 5.dp)
        ) {
            var selectedIndex by remember {
                val ndx = seriesDatesParams.options.indexOf( seriesDatesParams.selectedOption )
                mutableStateOf(ndx)
            }
            LargeDropdownMenu(
                label = stringResource(Res.string.seriesDateLabel),
                items = seriesDatesParams.options,
                selectedIndex = selectedIndex,
                selectedItemToString = {
                    it.value
                },
                onItemSelected = { index, option ->
                    selectedIndex = index
                    onBattingEvent(BattingSearchEvent.SeriesDateSelectionEvent(option))
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
                    item.value.lowercase().startsWith(filter.lowercase())
                }
            )
        }
    }
}