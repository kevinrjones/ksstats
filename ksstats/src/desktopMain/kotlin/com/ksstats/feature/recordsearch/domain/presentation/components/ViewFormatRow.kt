package com.ksstats.feature.recordsearch.domain.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ksstats.core.presentation.components.HorizontalTextRadioButton
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.presentation.BattingSearchEvent
import com.ksstats.feature.recordsearch.feature.mainbattingsearch.presentation.SearchViewFormat
import com.ksstats.ksstats.generated.resources.Res
import com.ksstats.ksstats.generated.resources.viewFormatLabel
import org.jetbrains.compose.resources.stringResource

@Composable
fun ViewFormatRow(
    searchViewFormat: SearchViewFormat,
    onBattingEvent: (BattingSearchEvent) -> Unit = {},
) {
    Row(modifier = Modifier.padding(bottom = 10.dp)) {
        Box {
            Text(stringResource(Res.string.viewFormatLabel))
        }
        Column(modifier = Modifier.weight(1f)) {
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.PlayerSummary,
                text = stringResource(SearchViewFormat.PlayerSummary.format),
                onOptionSelected = {
                    onBattingEvent(BattingSearchEvent.SearchViewFormatEvent(SearchViewFormat.PlayerSummary))
                })
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.InningsByInnings,
                text = stringResource(SearchViewFormat.InningsByInnings.format),
                onOptionSelected = {
                    onBattingEvent(BattingSearchEvent.SearchViewFormatEvent(SearchViewFormat.InningsByInnings))
                })
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.MatchTotals,
                text = stringResource(SearchViewFormat.MatchTotals.format),
                onOptionSelected = {
                    onBattingEvent(BattingSearchEvent.SearchViewFormatEvent(SearchViewFormat.MatchTotals))
                })
        }
        Column(modifier = Modifier.weight(1f)) {
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.SeriesAverages,
                text = stringResource(SearchViewFormat.SeriesAverages.format),
                onOptionSelected = {
                    onBattingEvent(BattingSearchEvent.SearchViewFormatEvent(SearchViewFormat.SeriesAverages))
                })
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.GroundAverages,
                text = stringResource(SearchViewFormat.GroundAverages.format),
                onOptionSelected = {
                    onBattingEvent(BattingSearchEvent.SearchViewFormatEvent(SearchViewFormat.GroundAverages))
                })
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.ByHostCountry,
                text = stringResource(SearchViewFormat.ByHostCountry.format),
                onOptionSelected = {
                    onBattingEvent(BattingSearchEvent.SearchViewFormatEvent(SearchViewFormat.ByHostCountry))
                })
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.ByOppositionTeam,
                text = stringResource(SearchViewFormat.ByOppositionTeam.format),
                onOptionSelected = {
                    onBattingEvent(BattingSearchEvent.SearchViewFormatEvent(SearchViewFormat.ByOppositionTeam))
                })
        }
        Column(modifier = Modifier.weight(1f)) {
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.ByYearOfMatchStart,
                text = stringResource(SearchViewFormat.ByYearOfMatchStart.format),
                onOptionSelected = {
                    onBattingEvent(BattingSearchEvent.SearchViewFormatEvent(SearchViewFormat.ByYearOfMatchStart))
                })
            HorizontalTextRadioButton(
                selected = searchViewFormat == SearchViewFormat.BySeason,
                text = stringResource(SearchViewFormat.BySeason.format),
                onOptionSelected = {
                    onBattingEvent(BattingSearchEvent.SearchViewFormatEvent(SearchViewFormat.BySeason))
                })
        }

    }
}