package com.ksstats.feature.recordsearch.domain.presentation.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ksstats.core.presentation.components.AppDropDownMenu
import com.ksstats.core.presentation.components.AppDropDownParams
import com.ksstats.feature.recordsearch.feature.mainsearch.search.presentation.MainSearchEvent


@Preview
@Composable
fun MatchTypeRowPreview() {
    MatchTypeRow(matchTypeParams = AppDropDownParams(label = "Match type"),
        competitionParams = AppDropDownParams(label = "Competition"),
        pageSizesParams = AppDropDownParams(label = "Page size"),
        minimumRuns = 0,
        minimumRunsLabel = "Minmum runs",
        onBattingEvent = {}
    )
}

@Composable
fun MatchTypeRow(
    matchTypeParams: AppDropDownParams,
    competitionParams: AppDropDownParams,
    pageSizesParams: AppDropDownParams,
    minimumRuns: Int,
    minimumRunsLabel: String,
    onBattingEvent: (MainSearchEvent) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp, end = 5.dp)
        ) {
            AppDropDownMenu(
                options = matchTypeParams.options,
                selectedOption = matchTypeParams.selectedOption,
                label = matchTypeParams.label,
                onSelectMenuItem = { onBattingEvent(MainSearchEvent.MatchTypeSelectionEvent(it)) }
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp, end = 5.dp)
        ) {
            AppDropDownMenu(
                options = competitionParams.options,
                selectedOption = competitionParams.selectedOption,
                label = competitionParams.label,
                onSelectMenuItem = { onBattingEvent(MainSearchEvent.CompetitionSelectionEvent(it)) }
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp, end = 5.dp)
        ) {
            AppDropDownMenu(
                options = pageSizesParams.options,
                selectedOption = pageSizesParams.selectedOption,
                label = pageSizesParams.label,
                onSelectMenuItem = { state ->
                    onBattingEvent(MainSearchEvent.PageSizeChangedEvent(state.value))
                }
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                modifier = Modifier,
                value = minimumRuns.toString(),
                onValueChange = { onBattingEvent(MainSearchEvent.MinimumValueChangedEvent(it)) },
                label = { Text(minimumRunsLabel) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
    }
}