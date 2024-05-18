package com.ksstats.feature.mainbatting.search.presentation.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ksstats.core.presentation.components.AppDropDownMenu
import com.ksstats.core.presentation.components.AppDropDownParams
import com.ksstats.feature.mainbatting.presentation.BattingSearchEvent
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


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
    onBattingEvent: (BattingSearchEvent) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp, end = 5.dp)
        ) {
            AppDropDownMenu(
                options = matchTypeParams.options,
                matchTypeParams.selectedOption,
                matchTypeParams.label,
                onSelectMenuItem = { onBattingEvent(BattingSearchEvent.MatchTypeSelectionEvent(it)) }
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp, end = 5.dp)
        ) {
            AppDropDownMenu(
                options = competitionParams.options,
                competitionParams.selectedOption,
                competitionParams.label,
                onSelectMenuItem = { onBattingEvent(BattingSearchEvent.CompetitionSelectionEvent(it)) }
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp, end = 5.dp)
        ) {
            AppDropDownMenu(
                options = pageSizesParams.options,
                pageSizesParams.selectedOption,
                pageSizesParams.label,
                onSelectMenuItem = {}
            )
        }
        Column(modifier = Modifier.weight(1f)) {

            OutlinedTextField(
                modifier = Modifier,
                value = minimumRuns.toString(),
                onValueChange = { onBattingEvent(BattingSearchEvent.MinimumValueChangedEvent(it)) },
                label = { Text(minimumRunsLabel) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
    }
}